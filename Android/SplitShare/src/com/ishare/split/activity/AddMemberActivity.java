package com.ishare.split.activity;

import java.util.Calendar;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;
import com.ishare.split.vo.UserSessionVO;
import com.ishare.split.vo.UserVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddMemberActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button submit;
	TextView userName;
	TextView userEmail;
	TextView userPhone;
	ImageView homeImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		userName = (TextView)findViewById(R.id.userName);
		userEmail = (TextView)findViewById(R.id.userEmail);
		userPhone = (TextView)findViewById(R.id.userPhone);
		

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
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*if(("").equals(userName.getText().toString()) ||
						   ("").equals(userEmail.getText().toString()) ||
						   ("").equals(userPhone.getText().toString()))*/
				if(("").equals(userName.getText().toString()))
						{
							Toast toast = Toast.makeText(AddMemberActivity.this, "Enter Name", Toast.LENGTH_SHORT);
							toast.show();
						}
						
						else{
							
							UserVO userVO = new UserVO();
							//-------- Test User 1-----------//
							int userId = splitShareDB.getMaxUserSeq();
							userVO.setUserId(userId);
							userVO.setRoleId(CommonConstant.USER_USER_ROLEID);
							userVO.setCode(CommonConstant.USER_USER_CODE+userId);
							userVO.setName(userName.getText().toString());
							userVO.setEmail(userEmail.getText().toString());
							userVO.setPhone(userPhone.getText().toString());
							userVO.setEmergencyContactName(CommonConstant.DEFAULT_NA);
							userVO.setEmergencyContactPhone(CommonConstant.DEFAULT_NA);
							userVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
							userVO.setStatus(CommonConstant.STATUS_ACTIVE);
							splitShareDB.insertUser(userVO);
							splitShareDB.updateUserSeq(userId);
							Toast toast = Toast.makeText(AddMemberActivity.this, userName.getText().toString() +" added", Toast.LENGTH_SHORT);
							toast.show();
							
							Intent intent = new Intent(context, AdminActivity.class);
							intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
							startActivity(intent);	
						}
						
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_member, menu);
		return true;
	}

}
