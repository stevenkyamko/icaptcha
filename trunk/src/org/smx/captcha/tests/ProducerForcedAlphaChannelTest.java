package org.smx.captcha.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.Producer;
import org.smx.captcha.impl.BackgroundImageAssembler;
import org.smx.captcha.impl.BoxedbackgroundProducer;
import org.smx.captcha.impl.FactoryRandomImpl;
import org.smx.captcha.impl.GridBackgroundProducer;

import junit.framework.TestCase;

public class ProducerForcedAlphaChannelTest extends TestCase {
	protected OutputStream os; 	 				
	protected FactoryRandomImpl inst;
	protected IBackgroundProducer backBoxed;
	protected IBackgroundProducer backGrid;
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ProducerForcedAlphaChannelTest.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("setUp");
		inst=(FactoryRandomImpl)Producer.forName("org.smx.captcha.impl.FactoryRandomImpl");
		inst.setSize(8);
	
		Properties backProp;
		backGrid =  new GridBackgroundProducer();
		 backProp=new Properties();
		backProp.put("background","E3F1FD");
		backProp.put("frequency","20");
		
		backGrid.setProperties(backProp);
		
		backProp=new Properties();
		backBoxed =  new BoxedbackgroundProducer();
		backProp.put("background","E3F1FD");
		backProp.put("border-color","FF0080");
		backProp.put("intersect","true");
		backProp.put("maxboxes","5");
		backProp.put("minboxes","2");
		
		backBoxed.setProperties(backProp);
	
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		
		//Force use of Default Producer
		inst.setBackGroundImageProducer( null );
		System.out.println("tearDown");
	}
	
	/**
	 * Producer.FORCE_ALPHA_CHANNEL should be True at the end of this test
	 *
	 */
	public void testForcedAlphaChannelFromJPGFormat(){
		try {
			os = new FileOutputStream("c:/captcha/testForcedAlphaChannelFromJPGFormat.jpg");
			
			Properties props = new Properties(); 		
			props.put("format", "jpg");
			props.put("font", "Helvetica");
			props.put("fontsize", "28");
			props.put("min-width", "180");
			props.put("padding-x", "25");
			props.put("padding-y", "25");
		
			BackgroundImageAssembler backgroundAssembler=new BackgroundImageAssembler();				
			backgroundAssembler.registerBackgroundProducer( backBoxed );
			backgroundAssembler.registerBackgroundProducer( backGrid );
			inst.setBackGroundImageProducer( backgroundAssembler );
				
			Producer.render(os, inst, props);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		assertTrue(Producer.FORCE_ALPHA_CHANNEL);
	}

	/**
	 * Producer.FORCE_ALPHA_CHANNEL should be False as we 
	 * are not using BackgroundImageAssembler and JPG don't have alpha channel
	 *
	 */
	public void testDetectAlphaChannelFromJPGFormat(){
		try {
			os = new FileOutputStream("c:/captcha/testDetectAlphaChannelFromJPGFormat.jpg");
			
			Properties props = new Properties(); 		
			props.put("format", "jpg");
			props.put("font", "Helvetica");
			props.put("fontsize", "28");
			props.put("min-width", "180");
			props.put("padding-x", "25");
			props.put("padding-y", "25");
		
			Producer.render(os, inst, props);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		assertFalse(Producer.FORCE_ALPHA_CHANNEL);
	}

	/**
	 * Producer.FORCE_ALPHA_CHANNEL should be False as we 
	 * are not using BackgroundImageAssembler and JPG don't have alpha channel
	 *
	 */
	public void testDetectAlphaChannelFromPNGFormat(){
		try {
			os = new FileOutputStream("c:/captcha/testDetectAlphaChannelFromPNGFormat.png");
			
			Properties props = new Properties(); 		
			props.put("format", "png");
			props.put("font", "Helvetica");
			props.put("fontsize", "28");
			props.put("min-width", "180");
			props.put("padding-x", "25");
			props.put("padding-y", "25");
		
			Producer.render(os, inst, props);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		assertFalse(Producer.FORCE_ALPHA_CHANNEL);
	}
	
	/**
	 * Producer.FORCE_ALPHA_CHANNEL should be True at the end of this test
	 *
	 */
	public void testForcedAlphaChannelFromPNGFormat(){
		try {
			os = new FileOutputStream("c:/captcha/testForcedAlphaChannelFromPNGFormat.png");
			
			Properties props = new Properties(); 		
			props.put("format", "png");
			props.put("font", "Helvetica");
			props.put("fontsize", "28");
			props.put("min-width", "180");
			props.put("padding-x", "25");
			props.put("padding-y", "25");
		
			BackgroundImageAssembler backgroundAssembler=new BackgroundImageAssembler();				
			backgroundAssembler.registerBackgroundProducer( backBoxed );
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
