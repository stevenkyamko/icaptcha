/**
 * 
 */
package org.smx.captcha.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;

public class CleanbackgroundProducer implements IBackgroundProducer{
	 private Properties props;
	 public CleanbackgroundProducer(){
		 props=new Properties();
	 }
	 public BufferedImage addBackground(BufferedImage image) {
		 Graphics2D g2=image.createGraphics();
		 int bacgroundRGB=Integer.parseInt(props.getProperty("background","E3F1FD"), 16);
		 int width=image.getWidth();
		 int height=image.getHeight();
	     g2.setColor(new Color(bacgroundRGB));
	     g2.fillRect(0,0, width,height);
		return image;
	}

	public void setProperties(Properties props) {
		this.props=props;			
	}
	
}