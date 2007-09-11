package org.smx.captcha.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.util.ImageHelper;

public class GridBackgroundProducer  implements IBackgroundProducer{
	private Properties props;
	public GridBackgroundProducer() {
		super();
		this.props=new Properties();
	}

	public BufferedImage addBackground(BufferedImage image) {
		int bacgroundRGB=Integer.parseInt(props.getProperty("background","E3F1FD"), 16);
		int gridDim=Integer.parseInt(props.getProperty("frequency","15"));
		
		int width=image.getWidth();
		int height=image.getHeight();		
		
		 BufferedImage bimage=ImageHelper.createCompatibleImage(image);
		 Graphics2D g2=bimage.createGraphics();		 
		 		 
		 // Set best alpha interpolation quality
		 g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		 // Clear the image so all pixels have zero alpha
		 g2.setComposite(AlphaComposite.Clear);
		 g2.fillRect(0, 0, width, height);
		 g2.setComposite(AlphaComposite.Src);
	     
	     int bacgroundGridRGB=0xFFFFFF;		     
	     int a_rgb,r_gb,g_gb,b_gb;
	     a_rgb=bacgroundRGB >>24 & 0xFF;
	     r_gb=bacgroundRGB  >>16 & 0xFF;
		 g_gb=bacgroundRGB  >>8  & 0xFF;
		 b_gb=bacgroundRGB  >>0  & 0xFF;
		
		
		 bacgroundRGB=(a_rgb<<24)|(r_gb<<16)|(g_gb<<8)|(b_gb<<0);
		 r_gb-=25;
		 g_gb-=25;
		 b_gb-=25;
		 //Make sure we are in Range
		 if(r_gb<0){
			 r_gb=0;
		 }
		 if(g_gb<0){
			 g_gb=0;
		 }
		 if(b_gb<0){
			 b_gb=0;
		 }
	     //pack the color 
		 bacgroundGridRGB=(a_rgb<<24)|(r_gb<<16)|(g_gb<<8)|(b_gb<<0);
		
		 g2.setColor(new Color(bacgroundRGB));
	     g2.fillRect(0, 0, width, height);
	    
		 
	     //create background grid color by increassing its rgb value
	     g2.setColor(new Color(bacgroundGridRGB));		     
	     int gridLineCountHorizontal = height / gridDim;		     
	     for(int k=1;k<gridLineCountHorizontal;k++){
	    	 g2.fillRect(0,k*gridDim,width,1);
	     }
	     int gridLineCountVertical = width / gridDim;		     
	     for(int k=1;k<gridLineCountVertical;k++){
	    	 g2.fillRect(k*gridDim, 0, 1, height);
	     }
	    g2.dispose();
	   
	    return bimage;
	}

	public void setProperties(Properties props) {
		this.props=props;
	}

}
