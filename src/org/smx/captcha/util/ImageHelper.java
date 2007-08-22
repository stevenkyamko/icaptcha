package org.smx.captcha.util;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.*;

import org.smx.captcha.Producer;
/**
 * Helper image class
 * @author gbugaj
 *
 */
public  class ImageHelper {
	
	private ImageHelper() {
		
	}
	/**
	 * Check if the image has Alpha Channel
	 * @param image
	 * @return
	 */
	public static boolean hasAlpha(java.awt.Image  image){
		if(image instanceof BufferedImage) {
			BufferedImage bim=(BufferedImage)image;
			return bim.getColorModel().hasAlpha();			
		}		
		PixelGrabber px= new PixelGrabber(image,0,0,1,1,false);
		
		try {
			px.grabPixels();
		} catch (InterruptedException e) {
			
		}
		return px.getColorModel().hasAlpha();
	}
	/**
	 * Creates compatible BufferedImage
	 * @param image
	 * @return
	 */
	public static BufferedImage createCompatibleImage(Image image){
		int transparency = Transparency.OPAQUE;
		boolean hasAlpha=false;
		//This is set when the ouput is JPG but we are using BackgroundAssembler
		if(Producer.FORCE_ALPHA_CHANNEL){
			hasAlpha=true;
		}else{//Do Standart Detection for alpha Channel
			hasAlpha=ImageHelper.hasAlpha(image);
		}
		
		if(hasAlpha){
			transparency=Transparency.BITMASK;
		}		 
		BufferedImage bimage=null;		
		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd=ge.getDefaultScreenDevice();
			GraphicsConfiguration gc=gd.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth( null ),image.getWidth( null ), transparency);
		}catch(HeadlessException ex){//Where is the screen
			
		}
		// Create a buffered image using the default color model
		if(bimage==null){
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);			
		}
		return bimage;
	}
}
