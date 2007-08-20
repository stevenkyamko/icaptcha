package org.smx.captcha;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;

import org.smx.captcha.impl.BoxedbackgroundProducer;
import org.smx.captcha.impl.CleanbackgroundProducer;
import org.smx.captcha.impl.FactoryLanguageImpl;
import org.smx.captcha.impl.FactoryRandomImpl;
import org.smx.captcha.impl.GradientBackgroundProducer;

public class Driver {

	public Driver() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		Properties props = new Properties(); 
		props.put("format", "jpg");
		props.put("font", "Helvetica");
		props.put("fontsize", "28");
		props.put("min-width", "180");
		props.put("padding-x", "25");
		props.put("padding-y", "25");
		
		//Set the default locale to custom locale
		//Locale locale = new Locale("ru","RU");
		//Locale.setDefault(locale);
		for(int i=0;i<5 ;i++){
				long ts=System.currentTimeMillis();
				String filename=i+"_img_test.png";
				OutputStream os = new FileOutputStream("c:/captcha/"+filename);	 				
				
				FactoryRandomImpl inst=(FactoryRandomImpl)Producer.forName("org.smx.captcha.impl.FactoryRandomImpl");
				inst.setSize(6);
				
				/*
				FactoryLanguageImpl inst=(FactoryLanguageImpl)Producer.forName("org.smx.captcha.impl.FactoryLanguageImpl");
				inst.setLanguageDirectory("C:\\captcha\\lang");
				inst.setLanguage("EN");
				inst.setRange(5, 10);	
				*/
				IBackgroundProducer back;
				//back =  new CleanbackgroundProducer();
				//back =  new BoxedbackgroundProducer();
				back =  new GradientBackgroundProducer();
				
				Properties backProp=new Properties();
				backProp.put("background","E3F1FD");
				backProp.put("border-color","FF0080");
				backProp.put("intersect","true");
				backProp.put("maxboxes","20");
				backProp.put("minboxes","5");
				
				back.setProperties(backProp);
				inst.setBackGroundImageProducer( back );
				
				Producer.render(os, inst, props);
				/*
				System.out.println("inst hash="+inst.getHashCode());
				System.out.println("inst word="+new String(inst.word));
				System.out.println("Solved ="+inst.getHashCode(inst.word));
				*/		
				long te=System.currentTimeMillis()-ts;				
				System.out.println("time = "+te);
		}
	}
}
