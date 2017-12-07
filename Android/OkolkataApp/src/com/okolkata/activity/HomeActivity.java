package com.okolkata.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

//import com.example.testandroid.WebViewActivity;
import com.okolkata.constant.CommonConstant;
import com.okolkata.util.XMLParser;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HomeActivity extends ListActivity {
	
	Button latest;
	Button aboutUs;
	Button writeToUs;
	Button literature;
	Button aronyok;
	Button misc;
	Button galary;
	Button blog;
	List headlines;
	List links;
	List rssFeedLink;
	List descriptions;
	
	//Slider Button
	Button latestSlider;
	Button aboutUsSlider;
	Button writeToUsSlider;
	Button literatureSlider;
	Button aronyokSlider;
	Button miscSlider;
	Button galarySlider;
	Button blogSlider;
	ProgressBar feedLoaderProgressBar;
	TextView home_msg;
	ListView list;
	
	Map<String, List> parserResponseMap;
	List<String> imgUrls = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy); 
            }
		
		//Navigation Button
		latest = (Button)findViewById(R.id.latest);
		aboutUs = (Button)findViewById(R.id.aboutUs);
		writeToUs = (Button)findViewById(R.id.writeToUs);
		literature = (Button)findViewById(R.id.literature);
		aronyok = (Button)findViewById(R.id.aronyok);
		misc = (Button)findViewById(R.id.misc);
		galary = (Button)findViewById(R.id.galary);
		blog = (Button)findViewById(R.id.blog);
		
		//Slider Button
		latestSlider = (Button)findViewById(R.id.latestSlider);
		aboutUsSlider = (Button)findViewById(R.id.aboutUsSlider);
		writeToUsSlider = (Button)findViewById(R.id.writeToUsSlider);
		literatureSlider = (Button)findViewById(R.id.literatureSlider);
		aronyokSlider = (Button)findViewById(R.id.aronyokSlider);
		miscSlider = (Button)findViewById(R.id.miscSlider);
		galarySlider = (Button)findViewById(R.id.galarySlider);
		blogSlider = (Button)findViewById(R.id.blogSlider);
		
		home_msg = (TextView)findViewById(R.id.home_msg);
		feedLoaderProgressBar = (ProgressBar)findViewById(R.id.feedLoaderProgressBar);
		
		
		try {
			//Hide all sliders
			hideAllSlider();
			
			//load the latest feed
			showSlider(latestSlider);
			populateFeed(CommonConstant.latestFeedURL);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//--- Getting the latest Feed ---//
		latest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					hideAllSlider();
					showSlider(latestSlider);
					populateFeed(CommonConstant.latestFeedURL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			
		});
		
		//--- Getting the aboutUs Feed ---//
		aboutUs.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(aboutUsSlider);
							populateFeed(CommonConstant.aboutUSFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
		
		//--- Getting the writeToUs Feed ---//
		writeToUs.setOnClickListener(new View.OnClickListener() {
							
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(writeToUsSlider);
							populateFeed(CommonConstant.writeToUsFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
		//--- Getting the literature Feed ---//
		literature.setOnClickListener(new View.OnClickListener() {
							
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(literatureSlider);
							populateFeed(CommonConstant.literatureFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
		
		//--- Getting the aronyok Feed ---//
		aronyok.setOnClickListener(new View.OnClickListener() {
							
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(aronyokSlider);
							populateFeed(CommonConstant.aronyokFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
				
		//--- Getting the misc Feed ---//
		misc.setOnClickListener(new View.OnClickListener() {
							
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(miscSlider);
							populateFeed(CommonConstant.miscFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
				
		//--- Getting the galary Feed ---//
		galary.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(galarySlider);
							populateFeed(CommonConstant.galaryFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
				
		//--- Getting the blog Feed ---//
		blog.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							hideAllSlider();
							showSlider(blogSlider);
							populateFeed(CommonConstant.blogFeedURL);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
		
	}
	
	private void hideAllSlider() {
		home_msg.setVisibility(View.GONE);
		latestSlider.setVisibility(View.GONE);
		aboutUsSlider.setVisibility(View.GONE);
		writeToUsSlider.setVisibility(View.GONE);
		literatureSlider.setVisibility(View.GONE);
		aronyokSlider.setVisibility(View.GONE);
		miscSlider.setVisibility(View.GONE);
		galarySlider.setVisibility(View.GONE);
		blogSlider.setVisibility(View.GONE);
		feedLoaderProgressBar.setVisibility(View.GONE);
		
	}
	
	private void showSlider(Button slider)
	{
		feedLoaderProgressBar.setVisibility(View.VISIBLE);
		slider.setVisibility(View.VISIBLE);
	}

	//Latest Feed
	private void populateFeed(String urlLink) throws Exception {
		 
		 headlines = new ArrayList();
	     links = new ArrayList();
	     rssFeedLink = new ArrayList();
	     descriptions= new ArrayList();
	     
		try {
			
			parserResponseMap = XMLParser.parseXMLFromRSSFeed(urlLink);
			//System.out.println("parserResponseMap:: "+parserResponseMap);
			headlines = parserResponseMap.get(CommonConstant.feedHeadLine);
			links = parserResponseMap.get(CommonConstant.feedLink);
			descriptions = parserResponseMap.get(CommonConstant.feedDescription);
			
			ArrayAdapter adapter = new ArrayAdapter(this,
	        		R.layout.list_rss_feed,R.id.feed_header, headlines);
	         
	        setListAdapter(adapter);
	        
	        list=(ListView)findViewById(android.R.id.list);
	        /*CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
			list=(ListView)findViewById(R.id.list);
			list.setAdapter(adapter);*/
	        
	        feedLoaderProgressBar.setVisibility(View.GONE);
        } /*catch (MalformedURLException e) {
        	Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
 		   	toast.show();
            e.printStackTrace();
        } catch (XmlPullParserException e) {
        	Toast toast = Toast.makeText(this, "Failed To Load", Toast.LENGTH_SHORT);
 		   	toast.show();
            e.printStackTrace();
        } catch (IOException e) {
        	Toast toast = Toast.makeText(this, "Failed To Load", Toast.LENGTH_SHORT);
 		   	toast.show();
            e.printStackTrace();
        }*/catch(Exception e)
        {
        	Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
 		   	toast.show();
 		    toast = Toast.makeText(this, e.getClass().getName(), Toast.LENGTH_SHORT);
 		    toast.show();
        }
		
		
		
	}
	
	  public InputStream getInputStream(URL url) {
   	   try {
   	       return url.openConnection().getInputStream();
   	   } catch (IOException e) {
   		   Toast toast = Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT);
   		   toast.show();
   	       return null;
   	     }
   	}

	  @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) 
	    {
	    	//Uri uri = Uri.parse((String) links.get(position));
	    	String linkVal = (String) links.get(position);
	    	System.out.println("linkVal:: "+linkVal);
	    	//Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    	//startActivity(intent);
	    
	    	Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("URI", linkVal);
			startActivity(intent);
	    	

	    }
	  
	  
	  public Drawable downloadImage(String url) {

	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpGet request = new HttpGet(url);
	        try {
	            HttpResponse response = httpClient.execute(request);
	            InputStream stream = response.getEntity().getContent();
	            Drawable drawable = Drawable.createFromStream(stream, "src");
	            //drawable.
	            return drawable;
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	            return null;
	        } catch (IllegalStateException e) {
	            e.printStackTrace();
	            return null;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }

	    }
	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onResume() {
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A92BA")));
		//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3BC6F5")));
		
		super.onResume();
	}
}
