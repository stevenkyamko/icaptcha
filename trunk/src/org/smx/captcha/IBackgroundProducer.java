package org.smx.captcha;

import java.awt.image.BufferedImage;

public interface IBackgroundProducer<T> {
	public BufferedImage addBackground(BufferedImage image);
	/**
	 * BackGroundProducer specific properties like 'color', 'size' etc'
	 * @param props
	 */
	public void setProperties(java.util.Properties props);
}
