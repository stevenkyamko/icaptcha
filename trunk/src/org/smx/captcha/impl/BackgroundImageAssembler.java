package org.smx.captcha.impl;

import java.util.*;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.IImageAssembler;
import org.smx.captcha.util.ImageHelper;

/**
 * Assemble Multiple backgrounds into one
 * @author gbugaj
 *
 */
public class BackgroundImageAssembler implements IImageAssembler  {
	private Properties props;
	private List<IBackgroundProducer> producers;
	public BackgroundImageAssembler(){
		producers=new ArrayList<IBackgroundProducer>();
		props= new Properties();
	}
	public void registerBackgroundProducer(IBackgroundProducer<IBackgroundProducer> producer){		
		producers.add(producer);			
	}
	
	
public BufferedImage addBackground(BufferedImage image) {
	BufferedImage mergedImage=ImageHelper.createCompatibleImage(image);
	Graphics2D g2=mergedImage.createGraphics();	
	g2.setComposite( AlphaComposite.DstOver );
	// Set best alpha interpolation quality
	g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	for(IBackgroundProducer emiter:producers){	
		g2.drawImage( emiter.addBackground( image ) ,0,0, null);
	}
	
	return mergedImage;
}
	
	
	public void setProperties(Properties props) {
		this.props=props;	
	}
	
	
	
}
