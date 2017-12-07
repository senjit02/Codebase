package com.ishare.split.activity;

import java.util.Calendar;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;
import com.ishare.split.vo.GroupVO;
import com.ishare.split.vo.UserSessionVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddGroupActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button submit;
	EditText groupName;
	EditText groupDescription;
	EditText groupEstimatedCost;
	GroupVO groupVO;
	ImageView homeImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_group);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		submit = (Button)findViewById(R.id.submit);
		groupName = (EditText)findViewById(R.id.groupName);
		groupDescription = (EditText)findViewById(R.id.groupDescription);
		groupEstimatedCost = (EditText)findViewById(R.id.groupEstimatedCost);
		//homeImage = (ImageView)findViewById(R.id.homeImage);
		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		homeImage= (ImageView)iconLayout.findViewById(R.id.homeImage);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		
		//------------Home ----------//
		homeImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommonConstant.ADMIN_ACTIVITY);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);	
				
			}
		});
		//--------------------------//
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				try{
					String errorMsg="";
					if("".equals(groupName.getText().toString()))
						errorMsg="Enter group name";
					if("".equals(groupDescription.getText().toString()))
						errorMsg="Enter group description";
					if("".equals(groupEstimatedCost.getText().toString()))
						errorMsg="Enter estimated cost";
					
					
					if("".equals(errorMsg))
					{
						int groupId = splitShareDB.getMaxGroupSeq();
						groupVO = new GroupVO();
						groupVO.setGroupId(groupId);
						groupVO.setGroupName(groupName.getText().toString());
						groupVO.setGroupDescription(groupDescription.getText().toString());
						groupVO.setEstimatedCost(Integer.parseInt(groupEstimatedCost.getText().toString()));
						groupVO.setCreateId(userSessionVO.getSessionUserId());
						groupVO.setStatus(CommonConstant.STATUS_ACTIVE);
						groupVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
						splitShareDB.addGroup(groupVO);
						splitShareDB.updateGroupSeq(groupId);
						Toast toast = Toast.makeText(AddGroupActivity.this, groupName.getText().toString()+" added successfully", Toast.LENGTH_LONG);
						toast.show();
						Intent intent = new Intent(context, AdminActivity.class);
						intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
						startActivity(intent);	
					
					}
					else{
						Toast toast = Toast.makeText(AddGroupActivity.this, errorMsg, Toast.LENGTH_LONG);
						toast.show();
					}
						
				}catch(Exception e)
				{
					Toast toast = Toast.makeText(AddGroupActivity.this, "Error while Adding the group "+groupName.getText().toString(), Toast.LENGTH_LONG); 
					toast.show();
					e.printStackTrace();
					Log.d(CommonConstant.APPLICATION_NAME, CommonConstant.GENERIC_ERROR);
				}
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_group, menu);
		return true;
	}

}
