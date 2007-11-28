/**
 * 
 */
package org.smx.captcha.impl;
import java.awt.geom.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.util.ImageHelper;

import com.sun.imageio.plugins.common.ImageUtil;

public class BoxedbackgroundProducer implements IBackgroundProducer{
	 private Properties props;
	 public BoxedbackgroundProducer(){
		 props=new Properties();
	 }
	 public BufferedImage addBackground(BufferedImage image) {
		 int width=image.getWidth();
		 int height=image.getHeight();
		 int bacgroundRGB=Integer.parseInt(props.getProperty("background","E3F1FD"), 16);
		 int maxBoxes=Integer.parseInt(props.getProperty("maxboxes","4"));
		 int minBoxes=Integer.parseInt(props.getProperty("minboxes","2"));
		 boolean canIntersect=Boolean.parseBoolean( props.getProperty("intersect","false") );
		 boolean border=Boolean.parseBoolean( props.getProperty("border-visible","true") );
		 int borderColor=Integer.parseInt ( props.getProperty("border-color","000000"),16);
		 
		 BufferedImage bimage=ImageHelper.createCompatibleImage(image);
		 Graphics2D g2=bimage.createGraphics();		 
		
		 Random rand= new Random();
		 // Set best alpha interpolation quality
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, 
		RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		 
		 // Clear the image so all pixels have zero alpha
		 g2.setComposite(AlphaComposite.Clear);
		 g2.fillRect(0, 0, width, height);
		 
		 g2.setComposite(AlphaComposite.Src);

		
		 int randNumberOfBoxes=0;
		 do{
			 randNumberOfBoxes = rand.nextInt(maxBoxes);	 
		 }while(randNumberOfBoxes<minBoxes );
			 
				 
		  //set transparency on the color
		 int a_rgb,r_gb,g_gb,b_gb;
	     a_rgb=bacgroundRGB >>24 & 0xFF;
	     r_gb=bacgroundRGB  >>16 & 0xFF;
		 g_gb=bacgroundRGB  >>8  & 0xFF;
		 b_gb=bacgroundRGB  >>0  & 0xFF;
		
		 a_rgb=250;

		 
	     //pack the color 
		 bacgroundRGB=(a_rgb<<24)|(r_gb<<16)|(g_gb<<8)|(b_gb<<0);
		
		 
		  g2.setColor(new Color(bacgroundRGB));
		 // g2.fillRect(0,0, width,height);
	     
	     Rectangle[] rectangler=new Rectangle[randNumberOfBoxes];
	     for(int i=0;i<randNumberOfBoxes;i++)rectangler[i]=new Rectangle();
	 	 
	     Rectangle rect;
	     int index=0;
	     while(randNumberOfBoxes > 0){		    
	    	int x,y,w,h;
	    	x=0;
	    	y=0;
	    	w=0;
	    	h=0;
	    	int iter=0;
	    	boolean intersect=false;
	    	
	    	while(true && iter<100){		    
		    	x=rand.nextInt(width);
	    		y=rand.nextInt(height);
	    		while(h<=0)
	    			h=rand.nextInt(height  - y);
	    		while(w<=0)
	    			w=rand.nextInt(width - x);
	    		
	    		rect=new Rectangle(x,y,w,h);
	    	
	    		if(!canIntersect){//can't intersect
		    		for(int i=0;i<index;i++){
		    			intersect=rect.intersects( rectangler[i] );
		    			if(intersect){break;}
		     		}
	    		}else{
	    			intersect=false;
	    		}
	    		
	 			if(!intersect){
    				rectangler[index]=rect;
    				break;
    			}
	    	}
    		
	    	g2.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		    g2.fillRect(x, y, w, h);
		    
		    if(border){
		    	g2.setColor(new Color(borderColor));
			    g2.drawRect(x+1, y+1, w-1, h-1);			    
		    }
		    randNumberOfBoxes--;
		    index++;
	     }
		return bimage;
	}

	public void setProperties(Properties props) {
		this.props=props;			
	}
	
}