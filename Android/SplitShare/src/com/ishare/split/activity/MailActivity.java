package com.ishare.split.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.vo.MailVO;
import com.ishare.split.vo.UserSessionVO;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MailActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Cursor cursor;
	int groupId;
	ImageView homeImage;
	LinearLayout mail_activity;
	//CheckBox userCheckbox;
	Button submit;
	Map<Integer,String> mailMap ;
	MailVO mailVO;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		groupId = savedInstanceState.getInt(CommonConstant.TRANSFER_GROUP_ID);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		mailVO = (MailVO) savedInstanceState.getSerializable(CommonConstant.MAIL_VO);
		mail_activity = (LinearLayout)findViewById(R.id.mail_activity);
		mailMap = new HashMap<Integer,String>();
		submit = (Button)findViewById(R.id.submit);
		//------------Home ----------//
		//homeImage = (ImageView)findViewById(R.id.homeImage);
		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		homeImage = (ImageView)iconLayout.findViewById(R.id.homeImage);
			homeImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, CommonConstant.ADMIN_ACTIVITY);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					startActivity(intent);	
					
				}
			});
			//--------------------------//
		
		cursor = splitShareDB.getUserListByGroup(groupId);
		if(null!=cursor && cursor.moveToFirst())
		{
			do{
				CheckBox userCheckbox = new CheckBox(context);
				userCheckbox.setId(cursor.getInt(0));
				userCheckbox.setText(cursor.getString(1));
				userCheckbox.setContentDescription(cursor.getString(3));
				userCheckbox.setOnClickListener(getOnClickDoSomething(userCheckbox));
				mail_activity.addView(userCheckbox);
				
			}while(cursor.moveToNext());
		}
		
		/*submit = new Button(context);
		submit.setEms(5);
		submit.setText("SEND MAIL");*/
		//submit.setTop(10);
		submit.setOnClickListener(getOnClickSendMail(submit));
		//mail_activity.addView(submit);
	}

	private OnClickListener getOnClickSendMail(Button submit) {
		 
		return new View.OnClickListener() 
			{ 
				public void onClick(View v) 
				{
					if(null!=mailMap && mailMap.size()==0)
					{
						Toast.makeText(MailActivity.this, "Please select atleast one member", Toast.LENGTH_SHORT).show();			
					}
					else
						sendEmail(mailVO);
				}
			};
	 
	}
	
	protected void sendEmail(MailVO mailVO) {
	      //Log.i("Send email", "");
	      String[] TO = new String[mailMap.size()];
	      Iterator iterator = mailMap.keySet().iterator();
	      int i =0;
	      while(iterator.hasNext())
	      {
	    	  TO[i] = mailMap.get(iterator.next());
	    	  //System.out.println(TO[i]);
	    	  //if(i>0)
	    	//	  System.out.println(TO[i-1]);
	    	  i++;
	    	  
	      }
	      String[] CC = {""};
	     
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");
	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	      emailIntent.putExtra(Intent.EXTRA_CC, CC);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailVO.getMailSubject());
	      emailIntent.putExtra(Intent.EXTRA_TEXT, mailVO.getMailBody()+CommonConstant.SIGNATURE);
	      
	      
	      try {
	         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	         finish();
	        // Log.i("Finished sending email...", "");
	      }
	      catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(MailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }

	View.OnClickListener getOnClickDoSomething(final CheckBox userCheckbox) 
	{ 
		return new View.OnClickListener() 
			{ 
				public void onClick(View v) 
				{ 
					/*System.out.println("*************id******" + button.getId()); 
					System.out.println("and text***" + button.getText().toString());*/
					if(userCheckbox.isChecked())
					{
						mailMap.put(userCheckbox.getId(), userCheckbox.getContentDescription().toString());
					}
					if(!userCheckbox.isChecked())
					{
						mailMap.remove(userCheckbox.getId());
					}
					
					System.out.println(mailMap);
				}
			};
	 }
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mail, menu);
		return true;
	}

}
