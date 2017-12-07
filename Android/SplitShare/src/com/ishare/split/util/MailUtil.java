package com.ishare.split.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.ishare.split.activity.ExpenseHistoryActivity;
import com.ishare.split.vo.MailVO;

public class MailUtil extends Activity{
	
	public void sendEmail(MailVO mailVO) {
	      //Log.i("Send email", "");
	      //String[] TO = {"m.senjit@gmail.com"};
	      String[] CC = {""};
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");
	      emailIntent.putExtra(Intent.EXTRA_EMAIL, mailVO.getToAddress());
	      emailIntent.putExtra(Intent.EXTRA_CC, CC);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailVO.getMailSubject());
	      emailIntent.putExtra(Intent.EXTRA_TEXT, mailVO.getMailBody());
	      
	      try {
	         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	         finish();
	        // Log.i("Finished sending email...", "");
	      }
	      catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(MailUtil.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }

}
