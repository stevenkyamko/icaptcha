package org.smx.captcha.tests;


import java.util.Properties;

import org.smx.captcha.Producer;

import org.smx.captcha.impl.FactorySimpleMathImpl;

import junit.framework.TestCase;

public class FactorySimpleMathImplTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FactorySimpleMathImplTest.class);
	}

	/*
	 * Test method for 'org.smx.captcha.impl.FactorySimpleMathImpl.getWord()'
	 */
	public void testGetWord() throws Exception {
		FactorySimpleMathImpl inst=(FactorySimpleMathImpl)Producer.forName("org.smx.captcha.impl.FactorySimpleMathImpl");
		
		Properties props=new Properties();
		props.setProperty("min","1");
		props.setProperty("max","5");
		props.setProperty("symbols","3");
		
		inst.setProperties(props);
		assertNotNull( inst );
		
		System.out.println("HashSolved="+inst.getHashCode(inst.getWord()));
		System.out.println("Solved For="+inst.getLastWord());
	}

}
