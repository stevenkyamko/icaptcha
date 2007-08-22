package org.smx.captcha;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.MemoryImageSource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import org.smx.captcha.impl.BackgroundImageAssembler;
import org.smx.captcha.impl.BoxedbackgroundProducer;
import org.smx.captcha.impl.DefaultBackgroundImpl;


public class Producer {
	private static int  IMAGE_TYPE_BUFFERED;
	public static boolean FORCE_ALPHA_CHANNEL=false;
	/**
	 * We we load current plugin using current Thread 
	 * @param className
	 * @throws Exception 
	 * @ref www.javageeks.com/Papers/ClassForName/ClassForName.pdf+Class.forName&hl=en&ct=clnk&cd=3&gl=us&client=firefox-a
	 */
	public static IWordFactory forName(String className) throws Exception{
		Thread t=Thread.currentThread();
		ClassLoader cl=t.getContextClassLoader();
		Class  cls = cl.loadClass( className );
		Method inst=findGetInstance(cls);
		Object o=inst.invoke(null, new Object[]{});
		if(!(o instanceof IWordFactory)){
			throw new ClassCastException("Passed In class "+ className +" is not an instance of  org.smx.captcha.IWordFactory");
		}
		return (IWordFactory)o;
	}
	
	private static Method findGetInstance(Class cls)throws Exception
	{
		java.lang.reflect.Method[] methods = cls.getMethods();
		for (int i=0; i<methods.length; i++)
		{
			if (methods[i].getName().equals("getInstance"))
				return methods[i];
		}
		return null;
	}
	
	/**
	 * Renders new captcha and writes it to the output stream
	 * @param os
	 * @param inst
	 * @param props
	 * @return
	 * @throws Exception
	 */
	public static boolean render(OutputStream os, IWordFactory inst, Properties props)throws Exception {
		FORCE_ALPHA_CHANNEL=false;
		
		String format=props.getProperty("format", "jpg").toLowerCase();	
		if(!format.equals("jpg") && !format.equals("png")){//Invalid JPG | PNG Format
			IMAGE_TYPE_BUFFERED=BufferedImage.TYPE_INT_RGB;
			format="jpg";
		}else if(format.equals("png")){//Valid PNG Format	
			IMAGE_TYPE_BUFFERED=BufferedImage.TYPE_INT_ARGB;
		}else{//Valid JPG Format
			IMAGE_TYPE_BUFFERED=BufferedImage.TYPE_INT_RGB;
		}
		//PutBack fixed format
		props.put("format",format);
		System.out.println(IMAGE_TYPE_BUFFERED);
		return ImageIO.write(renderImage(inst, props), format, os);		
	}
	/**
	 * Renders the ImageHelper
	 * @param inst
	 * @param props
	 * @return
	 */
	private static BufferedImage renderImage(IWordFactory inst, Properties props){	
		IBackgroundProducer BackGroundProducer;
		int fontSize=25;
		int min_width=-1;
		int min_height=-1;
		String text=inst.getWord().toUpperCase();
		
		/*
		 *  Default Properties
		 *  format - PNG, JPG  default JPG
		 *  font - Java Font Name default "Helvetica"
		 *  fontsize - defaults to  20
		 *  min-height : mininum height of the image defaults to dynamic size -1
		 *  min-width : minimun width of the image defaults to dynamic size -1
		 *  padding-x : x padding default 20
		 *  padding-y : Defaults dynamicly to size of text/2
		 *  
		 */ 
		
		if(props==null){
			props=new Properties();
			console("Using Defaults");
		}
		String fontName=props.getProperty("font", Constants.DEFAULT_FONT_NAME);			 
		fontSize=Integer.parseInt(props.getProperty("fontsize", Constants.DEFAULT_FONT_SIZE));			 
		min_width=Integer.parseInt(props.getProperty("min-width", Constants.DEFAULT_IMGAE_WIDTH));
		min_height=Integer.parseInt(props.getProperty("min-height", Constants.DEFAULT_IMGAE_HEIGHT));
		
		BackGroundProducer=inst.getBackGroundImageProducer();			 
		//We use default implementation of BackGrdoundProducer
		if(BackGroundProducer==null){
			BackGroundProducer = new DefaultBackgroundImpl();
		}			 
		
		Font font = new  Font(fontName, Font.BOLD,  fontSize);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		// Create an image that supports arbitrary levels of transparency
		BufferedImage buffer = gc.createCompatibleImage(1, 1, IMAGE_TYPE_BUFFERED);		     
		
		Graphics2D g2 = (Graphics2D)buffer.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		FontRenderContext fc=g2.getFontRenderContext();
		Rectangle2D bounds=font.getStringBounds(text, fc);
		
		int yBounds=(int)bounds.getHeight();
		int xBounds=(int) bounds.getWidth();
		int padding_x=Integer.parseInt(props.getProperty("padding-x", Constants.DEFAULT_IMGAE_PADDING_X));
		int padding_y=Integer.parseInt(props.getProperty("padding-y", (yBounds/2)+""));
		
		//padding_y=30;
		
		// calculate the size of the text
		int width = xBounds +padding_x;
		int height = yBounds + padding_y;
		
		if(min_width>width){
			width=min_width;
			padding_x+=width-xBounds;
		}
		
		//prepare buffer with new widht and size
		buffer = new BufferedImage(width, height, IMAGE_TYPE_BUFFERED);
		//Recreating with new Size 
		g2 = buffer.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		AlphaComposite ac =  AlphaComposite.getInstance(AlphaComposite.SRC);
		g2.setComposite(ac);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);
		
		int fontColor=0x000000;
		g2.setFont(font);		     		     
		g2.setColor(new Color(fontColor));
		
		//Render background		     	
		//g2.drawImage(BackGroundProducer.addBackground( buffer ),0,0, null);
		
		//Need to force alpha channel on consequent images since the user 
		//might have selected JPG as output which does not support transparency 
		if (BackGroundProducer instanceof BackgroundImageAssembler && props.getProperty("format", "jpg").equals("jpg")) {
			Producer.FORCE_ALPHA_CHANNEL=true;
		}		
		
		g2.drawRenderedImage(BackGroundProducer.addBackground( buffer ), null);
		
		Rectangle2D letterBounds=null;		
		float  dt=0;
		int    i=0;
		int numberOfPoints=text.length();
		BezierPoint2D[] curve=new BezierPoint2D[numberOfPoints];
		BezierPoint2D[] cp = new BezierPoint2D[4];
		for(int k=0;k<cp.length;k++){
			cp[k]=new BezierPoint2D();
		}
		
		int startXPosition=padding_x/2;
		int loc=(yBounds/2);
		int diff=(height-yBounds)/2;			  
		
		int iMills=Math.abs((int)System.currentTimeMillis());
		int signA=( new Random().nextInt(iMills)%2==1)?1:(-1);
		
		// P1 Bottom 1
		cp[0].x=startXPosition;			  
		cp[0].y=loc+( new Random().nextInt(diff)*signA);							  
		
		int signB=( new Random().nextInt(iMills)%2==1)?1:(-1);
		//P2 Top 1
		cp[1].x=startXPosition;			  	
		cp[0].y=loc+(new Random().nextInt(diff)*signB);	
		
		int signC=( new Random().nextInt(iMills)%2==1)?1:(-1);
		if(signA==signC){
			signC=signC*-1;
		}
		// P3 TOP 2
		cp[2].x=width-startXPosition;
		cp[2].y=loc+(new Random().nextInt(diff)*signC);	
		
		//P4 Bottom 2			    
		int signD=( new Random().nextInt(iMills)%2==1)?1:(-1);
		cp[3].x=width-padding_x/2;			    
		cp[3].y=loc+(new Random().nextInt(diff)*signD);
		
		dt = 1.0f / ( numberOfPoints - 1 );
		for( i = 0; i < numberOfPoints; i++){
			curve[i] =  PointOnCubicBezier( cp, i*dt );
		}
		
		int xstart=startXPosition-(startXPosition/text.length());
		int startYPosition=padding_y/2;
		for(int k=0;k<curve.length;k++){			    	 			    
			letterBounds=font.getStringBounds(""+text.charAt(k), fc);			    	 			    	 
			int ystart=0;
			//Make sure that letters are only placed on visible canvas				    	
			ystart=Math.round(curve[k].y)-(int)letterBounds.getCenterY()+startYPosition;
			if(ystart>height){//Checks Bottom
				ystart=height;
			}				    	
			g2.drawString(""+text.charAt(k),xstart, ystart);			    	 
			xstart+=letterBounds.getBounds().getWidth();			    	
		}
		
		curve=new BezierPoint2D[numberOfPoints=numberOfPoints*10];
		dt = 1.0f / ( numberOfPoints - 1 );
		for( i = 0; i < numberOfPoints; i++){
			curve[i] = PointOnCubicBezier( cp, i*dt );
		}
		
		//Create the line accross the text will be kind of fuzzy
		font = new  Font("Helvetica", Font.BOLD + Font.ITALIC,  10);
		g2.setFont(font);
		int halfBoundHeight=yBounds/2;
		int index=0;
		for(int k=0;k<curve.length;k++){				    				    	
			if(k %10 == 0){			    	
				letterBounds=font.getStringBounds(""+text.charAt(index), fc);
				halfBoundHeight = (int)letterBounds.getHeight()/3;
				index++;
			}
			int xpos=(int)curve[k].x;
			int ypos=(int)curve[k].y+halfBoundHeight+startYPosition;
			int sign=(Math.random()>.5)?1:-1;	
			if(sign==-1)
				g2.drawRect(xpos,ypos-1, 1,1 );
			g2.drawRect(xpos,ypos, 1,1 );
		}
		
		//Now only if the pixel is black then we need to add some noise			 
		float data[] = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,
				0.0625f, 0.125f, 0.0625f };
		Kernel kernel = new Kernel(3, 3, data);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
				null);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if((Math.random()>.93)){
					int rgb=buffer.getRGB(x, y);					    		
					int r,g,b;					    		
					r=rgb>>16 & 0xFF;
					g=rgb>>8 & 0xFF;
					b=rgb>>0 & 0xFF;
					if(r==0 && g==0 && b==0){
						buffer.setRGB(x,y,0xFFFFFF);
					}
				}					    	
			}
		}
		
		return  convolve.filter(buffer, null);
	}
	
	public static void console(Object o){
		System.out.println(o);
	}
	/*
	 ComputeBezier fills an array of BezierPoint2D structs with the curve   
	 points generated from the control points cp. Caller must 
	 allocate sufficient memory for the result, which is 
	 <sizeof(BezierPoint2D) numberOfPoints>
	 void ComputeBezier( BezierPoint2D* cp, int numberOfPoints, BezierPoint2D* curve )
	 {
	 float   dt;
	 int   i;
	 
	 dt = 1.0 / ( numberOfPoints - 1 );
	 
	 for( i = 0; i < numberOfPoints; i++)
	 curve[i] = PointOnCubicBezier( cp, i*dt );
	 }
	 */
	
	
	/*
	 * http://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * 
	 cp is a 4 element array where:
	 cp[0] is the starting point, or P0 in the above diagram
	 cp[1] is the first control point, or P1 in the above diagram
	 cp[2] is the second control point, or P2 in the above diagram
	 cp[3] is the end point, or P3 in the above diagram
	 t is the parameter value, 0 <= t <= 1
	 */
	
	public static BezierPoint2D PointOnCubicBezier( BezierPoint2D cp[], float t )
	{
		float   ax, bx, cx;
		float   ay, by, cy;
		float   tSquared, tCubed;
		BezierPoint2D result=new BezierPoint2D();
		
		/* calculate the polynomial coefficients */
		
		cx = 3.0f * (cp[1].x - cp[0].x);
		bx = 3.0f * (cp[2].x - cp[1].x) - cx;
		ax = cp[3].x - cp[0].x - cx - bx;
		
		cy = 3.0f * (cp[1].y - cp[0].y);
		by = 3.0f * (cp[2].y - cp[1].y) - cy;
		ay = cp[3].y - cp[0].y - cy - by;
		
		/* calculate the curve point at parameter value t */
		
		tSquared = t * t;
		tCubed = tSquared * t;
		
		result.x = (ax * tCubed) + (bx * tSquared) + (cx * t) + cp[0].x;
		result.y = (ay * tCubed) + (by * tSquared) + (cy * t) + cp[0].y;
		
		return result;
	}
	
}


