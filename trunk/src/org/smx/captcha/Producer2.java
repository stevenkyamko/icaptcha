package org.smx.captcha;
import java.awt.Color;
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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;



public class Producer2 {


	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		
		/*
		 * 
		 Toolkit tk = Toolkit.getDefaultToolkit();
		
		int w = 250;
		int h = 250;
		int pix[] = new int[w * h];
		int index = 0;
		for (int y = 0; y < h; y++) {
		    int red = (y * 255) / (h - 1);
		    for (int x = 0; x < w; x++) {
			int blue = (x * 255) / (w - 1);
			pix[index++] = (255 << 24) | (red << 16) | blue;
		    }
		}
		   ImageHelper img = tk.createImage(new MemoryImageSource(w, h, pix, 0, w));
		 
			String filename="img_test.jpg";
		   //Write image to the Disk
			OutputStream os = new FileOutputStream("c:/"+filename);	  
			BufferedImage img2Write=createBufferedImage( img );
		*/
			
/*
			
	    		 // Create buffered image that does not support transparency
			    img2Write = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);			  
			    // Create a graphics context on the buffered image
			    Graphics2D g2d = img2Write.createGraphics();
			    g2d.setColor(new Color(150,150,150));
			    g2d.drawString("No ImageHelper ",5, 5);
			    g2d.dispose();
	*/		
				String filename="img_test.jpg";
				OutputStream os = new FileOutputStream("c:/"+filename);	 
				ImageIO.write(drawString("longstringtext"), "jpg", os);
	}
	
	public static BufferedImage createBufferedImage(Image image)
	{
	  BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null),
	  BufferedImage.TYPE_INT_RGB);
	  Graphics2D g = bi.createGraphics();
	  g.drawImage(image, 0, 0, null);	 
	  return bi;
	}
	
	
	private static BufferedImage drawString(final String text){
		  	 Font font = new Font("Tahoma", Font.PLAIN, 20);	
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     GraphicsDevice gs = ge.getDefaultScreenDevice();
		     GraphicsConfiguration gc = gs.getDefaultConfiguration();
		     // Create an image that supports arbitrary levels of transparency
		     BufferedImage buffer = gc.createCompatibleImage(1, 1, BufferedImage.TYPE_INT_RGB);		     		     
		     Graphics2D g2 = (Graphics2D)buffer.getGraphics();
			 
		     g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		                          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		   
		     FontRenderContext fc=g2.getFontRenderContext();
		     Rectangle2D bounds=font.getStringBounds(text, fc);
		     
		     int padding_x , padding_y;
		      	 padding_x=50;
		      	 padding_y=60;
		     
		     
		     // calculate the size of the text
		     int width = (int) bounds.getWidth()+padding_x;
		     int height = (int) bounds.getHeight()+padding_y;
		     
		     //prepare some output
		     buffer = new BufferedImage(width, height,
		                                BufferedImage.TYPE_INT_RGB);
		
  
		     
		     g2 = buffer.createGraphics();
		     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                         RenderingHints.VALUE_ANTIALIAS_ON);
	     
		     g2.setColor(new Color(0xFFFFFF));
		     g2.fillRect(0,0, width,height);
		     
		     g2.setFont(font);		     
		     g2.setColor(new Color(0,0,0));
		    
		   
		     int size=text.length();
		     double deg=(double)360/(size*Math.PI);
		     System.out.println(" deg = "+deg);
		    
		     Rectangle2D letterBounds=null;
		     int xstart=20;
		     
		   
		     System.out.println("width="+width);
		     System.out.println("height="+height);
		     
		     /*
		     for(int i=1;i<=size;i++){
		    	 int diff=(int)Math.abs(Math.round(Math.sin(deg)*2));
		    	 
		    	 letterBounds=font.getStringBounds(""+text.charAt(i-1), fc);
		    	 
		    	 System.out.println(letterBounds.getBounds().getWidth()+"  : "+xstart+" : "+i+","+Math.sin(deg*i) +"   > "+diff);
		    	 int ystart=centerY+diff;
		    	 g2.drawString(""+text.charAt(i-1),xstart, ystart);
		    	 
		    	 xstart+=letterBounds.getBounds().getWidth();
		     }
		     */
		     
	
		     	float   dt=0;
			    int   i=0;
			    int numberOfPoints=text.length();
			    BezierPoint2D[] curve=new BezierPoint2D[numberOfPoints];
			    
			    
			    BezierPoint2D[] cp = new BezierPoint2D[4];
			    for(int k=0;k<cp.length;k++){
			    	cp[k]=new BezierPoint2D();
			    }
			    
			    //Bottom 1
			    cp[0].x=0;
			    cp[0].y=height;
			    //Top 1
			    cp[1].x=0;
			    cp[1].y=30;
			    
			    //TOP 2
			    cp[2].x=width-30;
			    cp[2].y=30;
			    
			    //BOTTOM 2
			    cp[3].x=width-20;
			    cp[3].y=height-20;
			    
			    
			    dt = 1.0f / ( numberOfPoints - 1 );
			    for( i = 0; i < numberOfPoints; i++){
			        curve[i] = pointOnCubicBezier( cp, i*dt );
			    }
		     		  
			    for(int k=0;k<curve.length;k++){
			    	System.out.println(curve[k].x + "  < - > "+curve[k].y);
			    	 
			    	 letterBounds=font.getStringBounds(""+text.charAt(k), fc);
			    	 
			    	 int ystart=Math.round(curve[k].y);
			    	 g2.drawString(""+text.charAt(k),xstart, ystart);
			    	 
			    	 xstart+=letterBounds.getBounds().getWidth();
			    }
			    
			  
			    //buffer.setRGB()
			    
		     return buffer;
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

public static BezierPoint2D pointOnCubicBezier( BezierPoint2D cp[], float t )
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



