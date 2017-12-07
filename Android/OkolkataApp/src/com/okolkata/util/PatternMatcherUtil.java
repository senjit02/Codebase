package com.okolkata.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcherUtil {

	/**
	 * @param args
	 */
	
	private final static String urlPattern = "src=\"([^\"]+)";
	
	public static String getUrlFromSrc(String source){
		
		String url = "";
		Pattern r = Pattern.compile(urlPattern);
		 Matcher m = r.matcher(source);
	      if (m.find( )) {
	         //System.out.println("Found value: " + m.group(0) );
	         //System.out.println("Found value: " + m.group(1) );
	         url = m.group(1);
	       //  System.out.println("Found value: " + m.group(2) );
	      }else {
	         System.out.println("NO MATCH");
	      }
	      
	    System.out.println("url:: "+url);
		return url;
	}
}
