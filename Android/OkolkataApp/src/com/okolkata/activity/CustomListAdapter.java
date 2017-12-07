package com.okolkata.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {
	
	private final Activity context;
	private final String[] feedHeaders;
	private final String[] feeIconURLs;
	
	public CustomListAdapter(Activity context, String[] feedHeaders, String[] feeIconURLs) {
		super(context, R.layout.list_rss_feed, feedHeaders);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.feedHeaders=feedHeaders;
		this.feeIconURLs=feeIconURLs;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.list_rss_feed, null,true);
		
		TextView feed_header = (TextView) rowView.findViewById(R.id.feed_header);
		ImageView feed_icon = (ImageView) rowView.findViewById(R.id.feed_icon);
		//TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
		
		feed_header.setText(feedHeaders[position]);
		//feed_icon.setImageResource(feeIconURLs[position]);
		URL url = null;
		try {
			url = new URL(feeIconURLs[position]);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		feed_icon.setImageBitmap(bmp);
		return rowView;
		
	};

}
