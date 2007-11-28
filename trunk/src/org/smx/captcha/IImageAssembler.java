package org.smx.captcha;
/**
 * Silly interface
 * @author gbugaj
 *
 */
public interface IImageAssembler extends IBackgroundProducer{
	public void registerBackgroundProducer(IBackgroundProducer<IBackgroundProducer> producer);
}
