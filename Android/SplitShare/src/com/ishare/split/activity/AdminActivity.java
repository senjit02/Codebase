package com.ishare.split.activity;


import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.ReportUtil;
import com.ishare.split.vo.UserSessionVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends BaseActivity {

	ImageView addGroup;
	ImageView addMember;
	ImageView viewGroup;
	ImageView viewMember;
	ImageView expenseReport;
	ImageView help;
	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	ImageView logoutImage;
	View groupView;
	View helpView;
	ImageView crossImage;
	RadioGroup groupListRadioGroup;
	TextView groupListView;
	TextView headerMsg;
	Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		addGroup = (ImageView)findViewById(R.id.addGroup);
		addMember = (ImageView)findViewById(R.id.addMember);
		viewGroup = (ImageView)findViewById(R.id.viewGroup);
		viewMember = (ImageView)findViewById(R.id.viewMember);
		expenseReport= (ImageView)findViewById(R.id.expenseReport);
		headerMsg = (TextView)findViewById(R.id.headerMsg);
		//groupListRadioGroup = (RadioGroup)findViewById(R.id.groupListRadioGroup);
		help = (ImageView)findViewById(R.id.help);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		//logoutImage = (ImageView)findViewById(R.id.logoutImage);
		headerMsg.setText("Hello "+userSessionVO.getName()+" !\nWelcome to Split-N-Share Home Page");
		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		logoutImage = (ImageView)iconLayout.findViewById(R.id.logoutImage);
		//Add group
		addGroup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(context, AddGroupActivity.class);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);
				
			}
		});
		
		//Add member
		addMember.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Intent intent = new Intent(context, AddMemberActivity.class);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					startActivity(intent);
					
				}
			});
		
		//View group
		viewGroup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
			    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
				groupView = layoutInflater.inflate(R.layout.grouplist_layout, null);
			    final PopupWindow popupWindow = new PopupWindow(groupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			    popupWindow.setTouchable(true);
		        popupWindow.setFocusable(true);
			    popupWindow.showAtLocation(groupView, Gravity.CENTER, 2,10);
			    groupListRadioGroup  = (RadioGroup)groupView.findViewById(R.id.groupListGroup);
			    crossImage = (ImageView)groupView.findViewById(R.id.crossImage);
			    groupListView = (TextView)groupView.findViewById(R.id.groupList);
			    //groupListView.setText("Select one member and add expense the member has done!\n");
				Button submitPres = (Button)groupView.findViewById(R.id.submitPres);
				
				cursor = splitShareDB.getAllGroupList(CommonConstant.STATUS_ACTIVE);
				if(null!=cursor && cursor.moveToFirst())
				{
					do{
						RadioButton radioButton = new RadioButton(context);
						radioButton.setId(cursor.getInt(0));
						radioButton.setText(cursor.getString(1));
						radioButton.setTextColor(Color.BLACK);
						groupListRadioGroup.addView(radioButton);
					}while(cursor.moveToNext());
					submitPres.setVisibility(1);
					splitShareDB.closeDBConnection(cursor);
				}else{
					groupListView.setText("No group is available yet!");
					submitPres.setVisibility(-1);
				}
				
				submitPres.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(groupListRadioGroup.getCheckedRadioButtonId() == -1)
						{
							String msg = "PLEASE SELECT A GROUP";
							Toast toast = Toast.makeText(AdminActivity.this, msg,Toast.LENGTH_SHORT);
							toast.show();
						}else{
							int selectedGroupId = ((RadioButton)groupView.findViewById(groupListRadioGroup.getCheckedRadioButtonId())).getId();
							Intent intent = new Intent(context, ViewGroupActivity.class);
							intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
							intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
							popupWindow.dismiss();
							startActivity(intent);	
							

						}
					}
				});
				
				crossImage.setOnClickListener(new Button.OnClickListener(){

				     @Override
				     public void onClick(View v) {
				    	 popupWindow.dismiss();
				     }});
				
			
				
			}
		});
		
		//View Member
		viewMember.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, ViewMemberActivity.class);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);
				
			}
		});
		
		//Expense Report
		expenseReport.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
			    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
				groupView = layoutInflater.inflate(R.layout.grouplist_layout, null);
			    final PopupWindow popupWindow = new PopupWindow(groupView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    popupWindow.setTouchable(true);
		        popupWindow.setFocusable(true);
			    popupWindow.showAtLocation(groupView, Gravity.CENTER, 2,10);
			    groupListRadioGroup  = (RadioGroup)groupView.findViewById(R.id.groupListGroup);
			    RelativeLayout rl = (RelativeLayout)groupView.findViewById(R.id.crossImageLayout);
			    //crossImage = (ImageView)groupView.findViewById(R.id.crossImage);
			    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
			    groupListView = (TextView)groupView.findViewById(R.id.groupList);
			    //groupListView.setText("Select one member and add expense the member has done!\n");
				Button submitPres = (Button)groupView.findViewById(R.id.submitPres);
				
				cursor = splitShareDB.getAllGroupList(CommonConstant.STATUS_ACTIVE);
				if(null!=cursor && cursor.moveToFirst())
				{
					do{
						RadioButton radioButton = new RadioButton(context);
						radioButton.setId(cursor.getInt(0));
						radioButton.setText(cursor.getString(1));
						radioButton.setTextColor(Color.BLACK);
						groupListRadioGroup.addView(radioButton);
					}while(cursor.moveToNext());
					submitPres.setVisibility(1);
					splitShareDB.closeDBConnection(cursor);
				}else{
					groupListView.setText("No group is available yet!");
					submitPres.setVisibility(-1);
				}
				
				submitPres.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(groupListRadioGroup.getCheckedRadioButtonId() == -1)
						{
							String msg = "PLEASE SELECT A GROUP";
							Toast toast = Toast.makeText(AdminActivity.this, msg,Toast.LENGTH_SHORT);
							toast.show();
						}else{
							int selectedGroupId = ((RadioButton)groupView.findViewById(groupListRadioGroup.getCheckedRadioButtonId())).getId();
							String selectedGroupName = (String) ((RadioButton)groupView.findViewById(groupListRadioGroup.getCheckedRadioButtonId())).getText();
							Intent intent = new Intent(context, ViewReportActivity.class);
							intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
							intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
							intent.putExtra(CommonConstant.TRANSFER_GROUP_NAME, selectedGroupName);
							popupWindow.dismiss();
							startActivity(intent);	
						}
					}
				});
				
				crossImage.setOnClickListener(new Button.OnClickListener(){

				     @Override
				     public void onClick(View v) {
				    	 popupWindow.dismiss();
				     }});
				
			}
		});

		//help
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
			    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
				helpView = layoutInflater.inflate(R.layout.help_layout, null);
			    final PopupWindow popupWindow = new PopupWindow(helpView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    popupWindow.setTouchable(true);
		        popupWindow.setFocusable(true);
			    popupWindow.showAtLocation(helpView, Gravity.CENTER, 2,10);
			    TextView helpMessage = (TextView)helpView.findViewById(R.id.helpMessage);
			    helpMessage.setText(ReportUtil.getHelp());
			    //crossImage = (ImageView)helpView.findViewById(R.id.crossImage);
			    RelativeLayout rl = (RelativeLayout)helpView.findViewById(R.id.crossImageLayout);
			    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
			    crossImage.setOnClickListener(new Button.OnClickListener(){

				     @Override
				     public void onClick(View v) {
				    	 popupWindow.dismiss();
				     }});
				
			}
		});

		//logout
		logoutImage.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						Intent intent = new Intent(context, LoginActivity.class);
						splitShareDB.deleteSessionUser();
						startActivity(intent);
						
					}
				});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		moveTaskToBack(true);
	}

}
