package org.smx.captcha;

import java.util.Properties;

/**
 * All Custom WordGenerators need to extend this Class
 * @author gbugaj
 *
 */
public abstract class  IWordFactory {
	protected static IWordFactory instance;
	public String word;
	protected IBackgroundProducer BackgroundImageProducer;
	protected java.util.Properties props;
	public IWordFactory(){};
	public static IWordFactory getInstance(){ return null; };
	protected String getWord(){ return ""; };
	public String getLastWord(){ return word; };	
	public void setProperties(java.util.Properties props){
		this.props=props;
	}
	public Properties getProperties(){
		return this.props;
	}
	
	/**
	 * Custom Implementation of the hash code solver
	 * Most of the time this would be sufficient but with international 
	 * characters and encodings we need to handle that ourself
	 * @return
	 */
	public String getHashCode(){ return ""+word.hashCode(); };

	public String getHashCode(String str){ return ""+str.hashCode(); };
	
	public  void setBackGroundImageProducer(IBackgroundProducer background){
		this.BackgroundImageProducer=background;
	}
	
	public  IBackgroundProducer getBackGroundImageProducer(){
		return this.BackgroundImageProducer;
	}
}