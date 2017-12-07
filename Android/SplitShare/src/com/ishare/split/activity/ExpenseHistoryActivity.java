package com.ishare.split.activity;

import java.util.Calendar;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;
import com.ishare.split.util.MailUtil;
import com.ishare.split.vo.ExpenseVO;
import com.ishare.split.vo.MailVO;
import com.ishare.split.vo.UserSessionVO;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ExpenseHistoryActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button submit;
	ImageView homeImage;
	RadioGroup expenseListRadioGroup;
	Button modifyExpense;
	Button deleteExpense;
	Button mailButton;
	int groupId;
	int expenseId;
	Cursor cursor;
	ExpenseVO expenseVO;
	String statusMsg="";
	String mailBody = "Hello,\n\nExpense Details are listed below\n\n";
	int totalExpense = 0;
	String groupName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_history);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		groupId = savedInstanceState.getInt(CommonConstant.TRANSFER_GROUP_ID);
		splitShareDB = new SplitShareDB(context);
		modifyExpense = (Button)findViewById(R.id.modifyExpense);
		deleteExpense = (Button)findViewById(R.id.deleteExpense);
		mailButton = (Button)findViewById(R.id.mailButton);
		expenseListRadioGroup = (RadioGroup)findViewById(R.id.expenseListRadioGroup);
		//------------Home ----------//
//		homeImage = (ImageView)findViewById(R.id.homeImage);
		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		homeImage= (ImageView)iconLayout.findViewById(R.id.homeImage);

		homeImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommonConstant.ADMIN_ACTIVITY);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);	
				
			}
		});
		//--------------------------//
		
		//Get the expense
		getExpenseHistory(groupId);
		
		
		//Modify Expense
		modifyExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(expenseListRadioGroup.getCheckedRadioButtonId() == -1)
				{
					String msg = "PLEASE SELECT EXPENSE";
					Toast toast = Toast.makeText(ExpenseHistoryActivity.this, msg,Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
					expenseId = ((RadioButton)findViewById(expenseListRadioGroup.getCheckedRadioButtonId())).getId();
					expenseVO = new ExpenseVO();
					expenseVO.setExpenseId(expenseId);
					expenseVO.setGroupId(groupId);
					Intent intent = new Intent(context, ExpenseUpdateActivity.class);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					intent.putExtra(CommonConstant.TRANSFER_VO, expenseVO);
					startActivity(intent);	
				}
				
			}
		});
		
		//Delete Expense
		deleteExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(expenseListRadioGroup.getCheckedRadioButtonId() == -1)
				{
					String msg = "PLEASE SELECT EXPENSE";
					Toast toast = Toast.makeText(ExpenseHistoryActivity.this, msg,Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
					expenseId = ((RadioButton)findViewById(expenseListRadioGroup.getCheckedRadioButtonId())).getId();
					AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
					dlgAlert.setMessage(CommonConstant.DELETE_MSG);
					dlgAlert.setTitle(CommonConstant.DELETE_TITLE);
					dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			    	    public void onClick(DialogInterface dialog, int which) {			      	
			    	    	splitShareDB.deleteExpense(expenseId);
							String msg = "Successfully deleted";
							Toast toast = Toast.makeText(ExpenseHistoryActivity.this, msg,Toast.LENGTH_SHORT);
							toast.show();
							Intent intent = new Intent(context, ExpenseHistoryActivity.class);
							intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
							intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, groupId);
							startActivity(intent);
			    	    }
			    	});
					dlgAlert.setNegativeButton("NO", null);
					dlgAlert.setCancelable(true);
					dlgAlert.create().show();
				//		
				}
				
			}
		});
		
		//mail expense
		mailButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						cursor = splitShareDB.getGroupDetails(groupId);
						if(null!=cursor && cursor.moveToFirst())
						{
							do{
								groupName = cursor.getString(1);
							}while(cursor.moveToNext());
						}
						splitShareDB.closeDBConnection(cursor);
						MailVO mailVO =new MailVO();
						mailVO.setMailBody(mailBody +"\n\nTotal expense is Rs. "+totalExpense+"/-\n");
						mailVO.setMailSubject("Split_N_Share | Group: "+groupName+" | Expense History as of "+DateUtil.getDateFormat(
								Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
						/*String[] TO = {"m.senjit@gmail.com"};
						mailVO.setToAddress(TO);
						*///-------
						/*System.out.println(mailVO.getToAddress());
						System.out.println(mailVO.getMailBody());
						System.out.println(mailVO.getMailSubject());*/
						//------
						//sendEmail(mailVO);
						Intent intent = new Intent(context, MailActivity.class);
						intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
						intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, groupId);
						intent.putExtra(CommonConstant.MAIL_VO, mailVO);
						startActivity(intent);	
						
					}
				});

	}

	private void getExpenseHistory(int groupId) {

		try{
			int counter = 1;
			cursor = splitShareDB.getGroupWiseExpenseReport(groupId);
			if(null!=cursor && cursor.moveToFirst())
			{
				
				do{
					RadioButton radioButton = new RadioButton(this);
					String status = cursor.getString(9);
					//--Get the expense type
					if(CommonConstant.EXPENSE_STATUS_EXPENSE.equals(status))
					{
						totalExpense = totalExpense + cursor.getInt(4);
						statusMsg = "Expense";
					}
					if(CommonConstant.EXPENSE_STATUS_ADVANCE.equals(status))
					{
						String receiverName = getReceiverName(cursor.getInt(6));
						statusMsg = "Advance to "+ receiverName;
					}
					//--------------
					radioButton.setId(cursor.getInt(0));
					radioButton.setText(cursor.getString(2)
							+"\n"
							+"Expense Description: "
							+cursor.getString(5)
							+"\n"
							+"Amount: Rs. "
							+cursor.getString(4)+"/-\n"
							+"Date: "
							+cursor.getString(7)+"\n"
							+"Expense Type: "
							+statusMsg
							+"\n\n");
					mailBody = mailBody 
							+ counter +". "
							+ cursor.getString(2)
							+"\n"
							+"Expense Description: "
							+cursor.getString(5)
							+"\n"
							+"Amount: Rs. "
							+cursor.getString(4)+"/-\n"
							+"Date: "
							+cursor.getString(7)+"\n"
							+"Expense Type: "
							+statusMsg
							+"\n\n";
							
					counter ++;
					expenseListRadioGroup.addView(radioButton);
				}while(cursor.moveToNext());
				deleteExpense.setVisibility(1);
				modifyExpense.setVisibility(1);
				mailButton.setVisibility(1);
			}
			else{
				deleteExpense.setVisibility(-1);
				modifyExpense.setVisibility(-1);
				mailButton.setVisibility(-1);
				Toast toast = Toast.makeText(ExpenseHistoryActivity.this, "No history available", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			Toast toast = Toast.makeText(ExpenseHistoryActivity.this, CommonConstant.GENERIC_ERROR, Toast.LENGTH_SHORT);
			toast.show();
		}finally{
			splitShareDB.closeDBConnection(cursor);
		}
		
	}

	
	
	private String getReceiverName(int receiverId) {
		
		String receiverName="";
		Cursor result=null;
		try{
			 result = splitShareDB.getUserDetailByUserId(receiverId);
			 if(null!=result && result.moveToFirst())
			 {
				 receiverName = result.getString(1);
			 }
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			splitShareDB.closeDBConnection(result);
			
		}
		return receiverName;
	}
	//----mail
	
	protected void sendEmail(MailVO mailVO) {
	      //Log.i("Send email", "");
	      //String[] TO = {"m.senjit@gmail.com"};
	      String[] CC = {""};
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");
	      emailIntent.putExtra(Intent.EXTRA_EMAIL, mailVO.getToAddress());
	      emailIntent.putExtra(Intent.EXTRA_CC, CC);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailVO.getMailSubject());
	      emailIntent.putExtra(Intent.EXTRA_TEXT, mailVO.getMailBody()+CommonConstant.SIGNATURE);
	      
	      
	      try {
	         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	         finish();
	        // Log.i("Finished sending email...", "");
	      }
	      catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(ExpenseHistoryActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_history, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent intent = new Intent(getBaseContext(), ViewGroupActivity.class);
		intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
		intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, groupId);
		startActivity(intent);	
		
	}

}
