package org.smx.captcha;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CleanFile {

	public static void main(String arg[]) {
		
		BufferedReader bis=null;
		BufferedReader encodingReader=null;
		String language="RU";
		try{
		
		  String apppath=getApplicationPath();
		  String sFilename=language+".dic";
		  File srcFile=new File(apppath+"\\src\\org\\smx\\captcha\\lang\\"+sFilename);			  
		  bis=new BufferedReader(new FileReader(srcFile));	
		 
		  File srcFileEncoding=new File(apppath+"\\src\\org\\smx\\captcha\\lang\\"+language+".aff");
		  encodingReader =  new BufferedReader(new FileReader(srcFileEncoding));
		  String encoding=encodingReader.readLine();
		  encoding = (encoding == null) ? "ISO-8859-1" : encoding;
		  String line=null;
		  
		  BufferedWriter bw=new BufferedWriter(new FileWriter(apppath+"\\src\\org\\smx\\captcha\\lang\\"+language+"_n.dic"));
		  
		  while((line=bis.readLine())!=null){
			  bw.write(line.split("/")[0]+"\n");
		  }
		  bw.close();
		  bis.close();
		  System.out.println("encoding="+encoding);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 
	 * @return path of the application
	 */
	private static String getApplicationPath() {
		File appPath = new File(CleanFile.class.getCanonicalName());
		try {
			appPath = appPath.getCanonicalFile().getParentFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appPath.toString();
	}
}
