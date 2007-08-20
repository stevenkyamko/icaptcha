package org.smx.captcha.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;
/**
 * Default implementation of IBackgroundProducer that will be used if none have been passed
 * @author gbugaj
 *
 */
public class DefaultBackgroundImpl implements IBackgroundProducer{

	public DefaultBackgroundImpl() {
	
	}

	public BufferedImage addBackground(BufferedImage image) {
		 Graphics2D g2=image.createGraphics();
		 int bacgroundRGB=0xE3F1FD;
		 int width=image.getWidth();
		 int height=image.getHeight();
	     g2.setColor(new Color(bacgroundRGB));
	     g2.fillRect(0,0, width,height);
	     
	     int bacgroundGridRGB=0xE3F1FD;		     
	     int r_gb,g_gb,b_gb;
	     
	     r_gb=bacgroundRGB>>16 & 0xFF;
		 g_gb=bacgroundRGB>>8 & 0xFF;
		 b_gb=bacgroundRGB>>0 & 0xFF;
		 
		 r_gb-=25;
		 g_gb-=25;
		 b_gb-=25;
		 
	     //pack the color 
		 bacgroundGridRGB=(r_gb<<16)|(g_gb<<8)|(b_gb<<0);
		 
	     //create background grid color by increassing its rgb value
	     g2.setColor(new Color(bacgroundGridRGB));		     
	     int gridDim=20;
	     
	     int gridLineCountHorizontal = height / gridDim;		     
	     for(int k=1;k<gridLineCountHorizontal;k++){
	    	 g2.fillRect(0,k*gridDim,width,1);
	     }
	     int gridLineCountVertical = width / gridDim;		     
	     for(int k=1;k<gridLineCountVertical;k++){
	    	 g2.fillRect(k*gridDim,0,1,height);
	     }
	
	    return image;
	}

	public void setProperties(Properties props) {
	
	}

}
