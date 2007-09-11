package org.smx.captcha.tests;


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
		assertNotNull( inst );
		
		inst.getHashCode(inst.getWord());
		System.out.println("Solved For="+inst.getLastWord());
	}

}
