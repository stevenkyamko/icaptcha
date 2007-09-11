package org.smx.captcha.impl;

import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;
import java.util.Stack;

import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.IWordFactory;
/**
 * Provide captchas in form of 
 * 1 + 2
 * 1 - 2
 * 3 / 5
 * 
 * Simple Expression Parser
 * @author gbugaj
 *
 */
public class FactorySimpleMathImpl extends IWordFactory {

	private String symbols[]={"+","-","*","/"};
	public static IWordFactory getInstance() {
		if(instance==null ){
			instance=new FactorySimpleMathImpl();
		}
		return instance;
	}
	
	public String getWord(){
		word="";
		int minDigit=1;
		int maxDigit=5;
		
		int numberOfSymbols=3;
	
		while(numberOfSymbols>0){
			String symbol=symbols[new Random().nextInt(symbols.length)];
			
			int leftDig=0;			
			do{
				leftDig=new Random().nextInt(maxDigit);
			}while(leftDig<minDigit);
			
			int rightDig=0;			
			do{
				rightDig=new Random().nextInt(maxDigit);
			}while(rightDig<minDigit);
		
			word+=leftDig+symbol+rightDig;
			numberOfSymbols--;
		}
		return word;
	}	
	public String getHashCode(String str){ 
		Stack opstack=new Stack();
		//Parse expresion into tokens [numbers,operands]
		String token="";
		
		while(!opstack.isEmpty()){
			System.out.println(opstack.pop());
		}
		
		return ""+str.hashCode(); 
	}

	private boolean isSymbol(char c) {	
		String s=c+"";		
		for(int i=0;i<symbols.length;i++){
			if( s .equals(symbols[i])){
				return true;
			}
		}
		return false;
	}
	
}
