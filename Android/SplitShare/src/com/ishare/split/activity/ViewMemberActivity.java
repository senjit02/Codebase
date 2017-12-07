package com.ishare.split.activity;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.vo.UserSessionVO;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMemberActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button submit;
	Button modifyUser;
	Button deleteUser;
	ImageView homeImage;
	RadioGroup userListRadioGroup;
	TextView headerMsg;
	int userId;
	String userName;
	Cursor cursor;
	String status;
	String statusText;
	Context thisContext;
	TextView headerMsg1;
	TextView headerMsg2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_member);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		thisContext = this;
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		splitShareDB = new SplitShareDB(context);
		userListRadioGroup = (RadioGroup)findViewById(R.id.userListRadioGroup);
		headerMsg = (TextView)findViewById(R.id.headerMsg);
		headerMsg1 = (TextView)findViewById(R.id.headerMsg1);
		headerMsg2 = (TextView)findViewById(R.id.headerMsg2);
		//------------Home ----------//
		//homeImage = (ImageView)findViewById(R.id.homeImage);

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
		submit = (Button)findViewById(R.id.submit);
		submit.setVisibility(-1);
		modifyUser = (Button)findViewById(R.id.modifyUser);
		deleteUser = (Button)findViewById(R.id.deleteUser);
		getMemberList();
		
		userListRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				
				int selectedUserId =  ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
				cursor = splitShareDB.getUserDetailByUserId(selectedUserId);
				cursor.moveToFirst();
				if(CommonConstant.STATUS_ACTIVE.equals(cursor.getString(9)))
				{
					submit.setVisibility(1);
					submit.setText("DEACTIVATE");
					status = CommonConstant.STATUS_INACTIVE;
					statusText = "deactivated";
				}
				else if(CommonConstant.STATUS_INACTIVE.equals(cursor.getString(9)))
				{
					submit.setVisibility(1);
					submit.setText("ACTIVATE");
					statusText = "activated";
					status = CommonConstant.STATUS_ACTIVE;
				}
				
			}
		});
		
	}

	private void getMemberList() {
		try{
			boolean isUserAdded = false;
			cursor = splitShareDB.getAllUserList();
			if(null!=cursor && cursor.moveToFirst())
			{
				
				do{
					
					if(cursor.getInt(0)!=CommonConstant.USER_ADMIN_ID)
					{
						isUserAdded = true;
						RadioButton radioButton = new RadioButton(this);
						String name = cursor.getString(1);
						if(CommonConstant.STATUS_ACTIVE.equals(cursor.getString(9)))
						{
							radioButton.setTextColor(Color.BLACK);
						}
						else if(CommonConstant.STATUS_INACTIVE.equals(cursor.getString(9)))
						{
							radioButton.setTextColor(Color.RED);
							name = name + " (Deactivated)";
						}
						radioButton.setId(cursor.getInt(0));
						radioButton.setText(name);
						userListRadioGroup.addView(radioButton);	
					}
					
				}while(cursor.moveToNext());
			}
			
			if(!isUserAdded)
			{
				headerMsg1.setText("   No User List Found. Please add user");
				headerMsg2.setVisibility(-1);
				submit.setVisibility(-1);
				modifyUser.setVisibility(-1);
				deleteUser.setVisibility(-1);
				Toast toast = Toast.makeText(ViewMemberActivity.this, "No user added yet", Toast.LENGTH_SHORT);
				toast.show();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Toast toast = Toast.makeText(ViewMemberActivity.this, CommonConstant.GENERIC_ERROR, Toast.LENGTH_SHORT);
			toast.show();
		}
		finally{
			splitShareDB.closeDBConnection(cursor);		
		}
		
		//---Activate/Deactivate user
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userListRadioGroup.getCheckedRadioButtonId() == -1)
				{

					String msg = "PLEASE SELECT A USER";
					Toast toast = Toast.makeText(ViewMemberActivity.this, msg,Toast.LENGTH_SHORT);
					toast.show();
				
				}else
				{
					userId = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
					userName = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getText().toString();
					splitShareDB.changeUserStatus(SplitShareDB.DATABASE_TABLE_USER,status,userId);
					userName = userName.replace("(Deactivated)", "");
					Toast toast = Toast.makeText(ViewMemberActivity.this, userName + " has been "+statusText,Toast.LENGTH_SHORT);
					toast.show();
					Intent intent = new Intent(getBaseContext(), ViewMemberActivity.class);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					startActivity(intent);
				}
				
			}
		});
		
		//----------Update user
		modifyUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userListRadioGroup.getCheckedRadioButtonId() == -1)
				{

					String msg = "PLEASE SELECT A USER";
					Toast toast = Toast.makeText(ViewMemberActivity.this, msg,Toast.LENGTH_SHORT);
					toast.show();
				
				}
				else{
					userId = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
					userName = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getText().toString();
					Intent intent = new Intent(getBaseContext(), UpdateMemberActivity.class);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					intent.putExtra(CommonConstant.TRANSFER_USER_ID, userId);
					intent.putExtra(CommonConstant.TRANSFER_USER_NAME, userName);
					startActivity(intent);
				}
				
			}
		});
		//---delete user
		
		deleteUser.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(userListRadioGroup.getCheckedRadioButtonId() == -1)
						{
		
							String msg = "PLEASE SELECT A USER";
							Toast toast = Toast.makeText(ViewMemberActivity.this, msg,Toast.LENGTH_SHORT);
							toast.show();
						
						}else
						{
							userId = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
							userName = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getText().toString();
							cursor = splitShareDB.getExpenseByUserId(userId);
							cursor.moveToFirst();
							if(0!=cursor.getInt(0))
							{
								System.out.println(cursor.getString(0));
								String msg = userName + " can not be deleted as user is already added in group(s)\nMember can only be deactivated"; 
								Toast toast = Toast.makeText(ViewMemberActivity.this,msg,Toast.LENGTH_LONG);
								toast.show();	
							}
							else
							{
								AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(thisContext);
								dlgAlert.setMessage(CommonConstant.DELETE_MSG);
								dlgAlert.setTitle(CommonConstant.DELETE_TITLE);
								dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
						    	    public void onClick(DialogInterface dialog, int which) {			      	
						    	    	splitShareDB.deleteUser(userId);
										Toast toast = Toast.makeText(ViewMemberActivity.this,userName +" is deleted successfully",Toast.LENGTH_SHORT);
										toast.show();	
										Intent intent = new Intent(getBaseContext(), ViewMemberActivity.class);
										intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
										startActivity(intent);	
						    	    }
						    	});
								dlgAlert.setNegativeButton("NO", null);
								dlgAlert.setCancelable(true);
								dlgAlert.create().show();
							
							}
						}
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_member, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent intent = new Intent(this, AdminActivity.class);
		intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
		startActivity(intent);
	}

}
