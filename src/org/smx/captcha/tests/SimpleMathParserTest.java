package org.smx.captcha.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.Producer;
import org.smx.captcha.impl.BackgroundImageAssembler;
import org.smx.captcha.impl.BoxedbackgroundProducer;
import org.smx.captcha.impl.FactoryRandomImpl;
import org.smx.captcha.impl.FactorySimpleMathImpl;
import org.smx.captcha.impl.GridBackgroundProducer;

import junit.framework.TestCase;

public class SimpleMathParserTest extends TestCase {

	private FileOutputStream os;
	protected FactorySimpleMathImpl inst;
	protected IBackgroundProducer backGrid;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SimpleMathParserTest.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("setUp");
		inst=(FactorySimpleMathImpl)Producer.forName("org.smx.captcha.impl.FactorySimpleMathImpl");
		
		Properties backProp;
		backGrid =  new GridBackgroundProducer();
		backProp=new Properties();
		backProp.put("background","E3F1FD");
		backProp.put("frequency","20");
		
		backGrid.setProperties(backProp);
	
		
		Properties instProps;
		instProps=new Properties();
		instProps.put("min","10");
		instProps.put("max","15");
		instProps.put("symbols","3");
				
		inst.setProperties(instProps);
	}
	
	/**
	 * Producer.FORCE_ALPHA_CHANNEL should be True at the end of this test
	 *
	 */
		public void testCheckMathParserCorrectness(){
		try {
			os = new FileOutputStream("c:/captcha/mathparser_0.png");
			
			Properties props = new Properties(); 		
			props.put("format", "png");
			props.put("font", "Helvetica");
			props.put("fontsize", "28");
			props.put("min-width", "180");
			props.put("padding-x", "25");
			props.put("padding-y", "25");
			//Disable Bezier curve drawing
			props.put("curve", "false");
		
			BackgroundImageAssembler backgroundAssembler=new BackgroundImageAssembler();				
			backgroundAssembler.registerBackgroundProducer( backGrid );
			inst.setBackGroundImageProducer( backgroundAssembler );
				
			Producer.render(os, inst, props);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		assertFalse(Producer.FORCE_ALPHA_CHANNEL);
	}

}
