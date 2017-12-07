package com.ishare.split.activity;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.vo.UserSessionVO;
import com.ishare.split.vo.UserVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMemberActivity extends BaseActivity {
	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button submit;
	int userId;
	String userName;
	ImageView homeImage;
	EditText userEmail;
	EditText userPhone;
	EditText userEditedName;
	Cursor cursor;
	TextView headerMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_member);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		userId = savedInstanceState.getInt(CommonConstant.TRANSFER_USER_ID);
		userName = savedInstanceState.getString(CommonConstant.TRANSFER_USER_NAME);
		userPhone = (EditText)findViewById(R.id.userPhone);
		userEmail = (EditText)findViewById(R.id.userEmail);
		userEditedName = (EditText)findViewById(R.id.userName);
		headerMsg = (TextView)findViewById(R.id.headerMsg);
		submit = (Button)findViewById(R.id.submit);
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
		headerMsg.setText("  Update details for "+userName);
		getUSerDetails(userId);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String errorMsg="";
				if("".equals(userEditedName.getText().toString()))
					errorMsg = "Enter Name";
				/*if("".equals(userEmail.getText().toString()))
					errorMsg = "Enter Email";
				if("".equals(userPhone.getText().toString()))
					errorMsg = "Enter Phone";*/
				
				
				if("".equals(errorMsg))
				{
					UserVO userVO = new UserVO();
					userVO.setUserId(userId);
					userVO.setName(userEditedName.getText().toString());
					userVO.setEmail(userEmail.getText().toString());
					userVO.setPhone(userPhone.getText().toString());
					splitShareDB.updateUser(userVO);
					Toast toast = Toast.makeText(UpdateMemberActivity.this, userName + " updated successfully", Toast.LENGTH_SHORT);
					toast.show();
					Intent intent = new Intent(context, ViewMemberActivity.class);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					startActivity(intent);
				}
				else
				{
					Toast toast = Toast.makeText(UpdateMemberActivity.this, errorMsg, Toast.LENGTH_SHORT);
					toast.show();
				}
				
			}
		});
	}

	private void getUSerDetails(int userId) {

		try{
			cursor = splitShareDB.getUserDetailByUserId(userId);
			if(null!=cursor && cursor.moveToFirst())
			{
				do{
					userEditedName.setText(cursor.getString(1));
					userEmail.setText(cursor.getString(4));
					userPhone.setText(cursor.getString(5));
				}while(cursor.moveToNext());
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			Toast toast = Toast.makeText(UpdateMemberActivity.this, CommonConstant.GENERIC_ERROR, Toast.LENGTH_SHORT);
			toast.show();
		}finally{
			splitShareDB.closeDBConnection(cursor);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_member, menu);
		return true;
	}

}
