package org.smx.captcha.impl;

import java.util.*;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.ImageAssembler;
import org.smx.captcha.util.ImageHelper;

/**
 * Assemble Multiple backgrounds into one
 * @author gbugaj
 *
 */
public class BackgroundImageAssembler implements ImageAssembler  {
	private Properties props;
	private List producers;
	public BackgroundImageAssembler(){
		producers=new ArrayList();
		props= new Properties();
	}
	public void registerBackgroundProducer(IBackgroundProducer producer) {
		producers.add( producer );			
	}
	
	
	public BufferedImage addBackground(BufferedImage image) {
		BufferedImage mergedImage=ImageHelper.createCompatibleImage(image);
		Graphics2D g2=mergedImage.createGraphics();	
		g2.setComposite( AlphaComposite.DstOver );
		// Set best alpha interpolation quality
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		for(int i=0;i<producers.size();i++){
			BufferedImage a=((IBackgroundProducer)producers.get( i )).addBackground( image );
			g2.drawImage( a ,0,0, null);
		}
		
		return mergedImage;
	}
	
	
	public void setProperties(Properties props) {
		this.props=props;	
	}
	
	
	
}
