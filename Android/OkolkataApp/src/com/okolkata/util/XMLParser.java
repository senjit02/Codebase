package com.okolkata.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.okolkata.activity.R;
import com.okolkata.constant.CommonConstant;

public class XMLParser {
	
	
	

	public static Map<String, List> parseXMLFromRSSFeed(String urlLink) throws Exception
	{

		 Map<String, List> parserResponseMap = new HashMap<String, List>();
		 List headlines = new ArrayList();
		 List links = new ArrayList();
		 List descriptions = new ArrayList();
		 List rssFeedLink = new ArrayList();
	     
		try {
       	//String[] urls = {"http://feeds.pcworld.com/pcworld/latestnews"};
           //URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
			URL url = new URL(urlLink);
        
           XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
           factory.setNamespaceAware(false);
           XmlPullParser xpp = factory.newPullParser();
        
               // We will get the XML from an input stream
           xpp.setInput(getInputStream(url), "UTF_8");
        
           //RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask();
           //retrieveFeedTask.execute(urls);
           
           
               /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
                * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
                * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
                * so we should skip the "<title>" tag which is a child of "<channel>" tag,
                * and take in consideration only "<title>" tag which is a child of "<item>"
                *
                * In order to achieve this, we will make use of a boolean variable.
                */
           boolean insideItem = false;
        
               // Returns the type of current event: START_TAG, END_TAG, etc..
           int eventType = xpp.getEventType();
           while (eventType != XmlPullParser.END_DOCUMENT) {
               if (eventType == XmlPullParser.START_TAG) {
        
                   if (xpp.getName().equalsIgnoreCase("item")) {
                       insideItem = true;
                   } else if (xpp.getName().equalsIgnoreCase("title")) {
                       if (insideItem)
                           headlines.add(xpp.nextText()); //extract the headline
                   } else if (xpp.getName().equalsIgnoreCase("link")) {
                       if (insideItem)
                           links.add(xpp.nextText()); //extract the link of article
                   }
                   else if (xpp.getName().equalsIgnoreCase("description")) {
                       if (insideItem)
                       {
                    	   //System.out.println("inside xml: "+xpp.nextText());
                    	   descriptions.add(PatternMatcherUtil.getUrlFromSrc(xpp.nextText())); //extract the img url from src
                       }
                   }
                   else if (xpp.getName().equalsIgnoreCase("wfw:commentRss")) {
                       if (insideItem)
                       	rssFeedLink.add(xpp.nextText()); //extract the rss link of article
                   }
               }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                   insideItem=false;
               }
        
               eventType = xpp.next(); //move to next element
           }
        
       } catch (MalformedURLException e) {       	
    	   throw new MalformedURLException("Failed To Load");
       } catch (XmlPullParserException e) {
    	   throw new XmlPullParserException("Failed To Load");
       } catch (IOException e) {
    	   throw new IOException("nei Internet Connection");
       }catch(Exception e)
       {
    	   throw new Exception("Failed To Load");
       }
		
		parserResponseMap.put(CommonConstant.feedHeadLine, headlines);
		parserResponseMap.put(CommonConstant.feedLink, links);
		parserResponseMap.put(CommonConstant.feedDescription, descriptions);
		
		return parserResponseMap; 
		
	
		
	}
	
	public static InputStream getInputStream(URL url) throws Exception {
	   	   try {
	   	       return url.openConnection().getInputStream();
	   	   } catch (IOException e) {
	   		   throw new Exception("No Internet Connection");
	   	     }
	   	   catch (Exception e) {
	   		   throw new Exception("Check Internet Connection");
	   	     }
	   	}
}
