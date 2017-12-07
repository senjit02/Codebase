package com.ishare.split.activity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;

import com.ishare.split.vo.GroupVO;
import com.ishare.split.vo.UserGroupVO;
import com.ishare.split.vo.UserSessionVO;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

public class ViewGroupActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	
	ImageView crossImage;
	View userView;
	View groupUpdateView;
	RadioGroup userListRadioGroup;
	int userId;
	//int currentGroupId;
	//RadioGroup groupListRadioGroup;
	Cursor cursor;
	int selectedGroupId;
	UserGroupVO userGroupVO;
	//Button getReport;
	ImageView addMember;
	ImageView viewMember;
	ImageView addExpense;
	ImageView getExpense;
	ImageView updateExpense;
	ImageView deleteGroup;
	ImageView groupUpdate;
	TextView userListView;
	TextView groupDetail;
	//TextView groupUpdate;
	TextView activityText;
	ImageView homeImage;
	EditText groupDescription;
	EditText groupEstimatedCost;
	String groupDescriptionText = "";
	String groupCostText = "";
	String selectedGroupName="";
	TextView userList;
	LinearLayout listLinearLayOut;
	Map<Integer, String> userMap = new HashMap<Integer, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_group);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		//groupListRadioGroup = (RadioGroup)findViewById(R.id.groupListRadioGroup);
		addMember = (ImageView)findViewById(R.id.addMember);
		//viewMember = (Button)findViewById(R.id.viewMember);
		addExpense = (ImageView)findViewById(R.id.addExpense);
		deleteGroup = (ImageView)findViewById(R.id.deleteGroup);
		//addExpense = (Button)findViewById(R.id.addExpense);
		//getReport = (Button)findViewById(R.id.getReport);
		updateExpense = (ImageView)findViewById(R.id.updateExpense);
		getExpense = (ImageView)findViewById(R.id.getExpense);;
		groupDetail = (TextView)findViewById(R.id.groupDetail);
		groupUpdate = (ImageView)findViewById(R.id.groupUpdate);
		viewMember = (ImageView)findViewById(R.id.viewMember);
		groupDetail.setVisibility(-1);
		groupUpdate.setVisibility(-1);
		//activityText = (TextView)findViewById(R.id.activityText);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		selectedGroupId = savedInstanceState.getInt(CommonConstant.TRANSFER_GROUP_ID);
		
		//------------Home ----------//
		homeImage = (ImageView)findViewById(R.id.homeImage);
		homeImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommonConstant.ADMIN_ACTIVITY);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);	
				
			}
		});
		//--------------------------//
		
		//Get the group list
		//getAllGroupList();
		
		
		String activity = "1. Select group to get the group details\n" +
				"2. Click on Add Member to add member to this group\n" +
				"3. Click on View Member to view member/add expense for member\n" +
				"4. Click on Report to view expense report for group\n" +
				"5. Click on Expense History to view expense history for group\n" +
				"6. Click on Delete to delete this group\n";
		
		// Get the group details
		getGroupDetail(selectedGroupId);
		
		
		//Member add to this group
		addMember.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					//selectedGroupId = ((RadioButton)findViewById(groupListRadioGroup.getCheckedRadioButtonId())).getId();	
					cursor = splitShareDB.getUserListToAdd(selectedGroupId);
					LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
				    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
					//userView = layoutInflater.inflate(R.layout.userlist_layout, null);
					userView = layoutInflater.inflate(R.layout.userchecklist_layout, null);
				    final PopupWindow popupWindow = new PopupWindow(userView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				    popupWindow.setTouchable(true);
			        popupWindow.setFocusable(true);  
				    popupWindow.showAtLocation(userView, Gravity.CENTER, 2,10);
				    //userListRadioGroup  = (RadioGroup)userView.findViewById(R.id.userListRadioGroup);
				    listLinearLayOut = (LinearLayout)userView.findViewById(R.id.listLinearLayOut);
				    //crossImage = (ImageView)userView.findViewById(R.id.crossImage);
				    RelativeLayout rl = (RelativeLayout)userView.findViewById(R.id.crossImageLayout);
				    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
				    TextView userListView = (TextView)userView.findViewById(R.id.userListView);
				    boolean flag = false;
					Button submitPres = (Button)userView.findViewById(R.id.submitPres);
					if(null!=cursor && cursor.moveToFirst())
					{
						do{
							if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0))
							{
								/*RadioButton radioButton = new RadioButton(context);
								String name = cursor.getString(1);
								radioButton.setText(name);
								radioButton.setId(cursor.getInt(0));
								userListRadioGroup.addView(radioButton);*/	
								flag = true;
								CheckBox userCheckbox = new CheckBox(context);
								String name = cursor.getString(1);
								userCheckbox.setText(name);
								userCheckbox.setId(cursor.getInt(0));
								listLinearLayOut.addView(userCheckbox);
								userCheckbox.setOnClickListener(getOnClickAddUser(userCheckbox));
							}
							
						}while(cursor.moveToNext());
						
						if(!flag)
						{
							userListView.setText("\t\tNo user is available to add to this group");
							submitPres.setVisibility(View.INVISIBLE);
						}
					}
					
					splitShareDB.closeDBConnection(cursor);
					submitPres.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
							System.out.println("userMap:: "+userMap);
							//if(userListRadioGroup.getCheckedRadioButtonId() == -1)
							if(userMap.isEmpty())
							{
								String msg = "Please select a user";
								Toast toast = Toast.makeText(ViewGroupActivity.this, msg,Toast.LENGTH_LONG);
								toast.show();
							}else{
								int userId =0;
								String userName="";
								try{
									//userId = ((RadioButton)userView.findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
									//userName = String.valueOf(((RadioButton)userView.findViewById(userListRadioGroup.getCheckedRadioButtonId())).getText());
									
									Iterator<Integer> iterator = userMap.keySet().iterator();
									userGroupVO = new UserGroupVO();
									userGroupVO.setGroupId(selectedGroupId);
									userGroupVO.setGroupRoleId(CommonConstant.GROUP_ROLE_MEMBER);
									userGroupVO.setCreateId(userSessionVO.getSessionUserId());
									userGroupVO.setStatus(CommonConstant.STATUS_ACTIVE);
									while(iterator.hasNext())
									{
										userId = iterator.next();
										userGroupVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
										userGroupVO.setUserId(userId);
										splitShareDB.insertGroupMember(userGroupVO);
									}
									
									Toast toast = Toast.makeText(ViewGroupActivity.this,  "User(s) Added Successfully ",Toast.LENGTH_LONG);
									toast.show();
									popupWindow.dismiss();
									Intent intent = new Intent(context, ViewGroupActivity.class);
									intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
									intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
									startActivity(intent);	
								}catch(Exception e)
								{
									Toast toast = Toast.makeText(ViewGroupActivity.this, CommonConstant.GENERIC_ERROR +" in adding "+userName,Toast.LENGTH_LONG);
									toast.show();
									e.printStackTrace();
								}
								
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
		
		
		//----------View member -----------------//
		viewMember.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String userText = "";
				cursor = splitShareDB.getUserListByGroup(selectedGroupId);
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
			    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
				userView = layoutInflater.inflate(R.layout.user_view_list_layout, null);
			    final PopupWindow popupWindow = new PopupWindow(userView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    popupWindow.setTouchable(true);
		        popupWindow.setFocusable(true);
			    popupWindow.showAtLocation(userView, Gravity.CENTER, 2,10);
			    //userListRadioGroup  = (RadioGroup)userView.findViewById(R.id.userListRadioGroup);
			    //crossImage = (ImageView)userView.findViewById(R.id.crossImage);
			    RelativeLayout rl = (RelativeLayout)userView.findViewById(R.id.crossImageLayout);
			    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
			    userListView = (TextView)userView.findViewById(R.id.userListView);
			    userList  = (TextView)userView.findViewById(R.id.userList);
				if(null!=cursor && cursor.moveToFirst())
				{
					userListView.setText("    List of member for this group is listed below\n\n");
					userListView.setTextColor(Color.BLACK);
					int counter = 1;
					do{
						if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0)){
							String name = cursor.getString(1);
							if(CommonConstant.STATUS_INACTIVE.equals(cursor.getString(6)))
							{
								name = name + " (Deactivated)";
								
							}
							
							userText = userText + counter + ". "+name +"\n";	
							counter ++;
						}
						
					}while(cursor.moveToNext());
					
					userList.setText(userText);	
				}else{
					userListView.setText("   Please add member to this group!");
					Toast toast = Toast.makeText(ViewGroupActivity.this, "No user added yet",Toast.LENGTH_SHORT);
					toast.show();
					userListView.setTextColor(Color.RED);
					//userListView.setText(userText);
					
				}
				
				splitShareDB.closeDBConnection(cursor);
				crossImage.setOnClickListener(new Button.OnClickListener(){

				     @Override
				     public void onClick(View v) {
				    	 popupWindow.dismiss();
				     }});
			}
		});
		
		//----------Add Expense -----------------//
		
		addExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
					cursor = splitShareDB.getUserListByGroup(selectedGroupId);
					LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
				    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
					userView = layoutInflater.inflate(R.layout.userlist_layout, null);
				    final PopupWindow popupWindow = new PopupWindow(userView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				    popupWindow.setTouchable(true);
			        popupWindow.setFocusable(true);
				    popupWindow.showAtLocation(userView, Gravity.CENTER, 2,10);
				    userListRadioGroup  = (RadioGroup)userView.findViewById(R.id.userListRadioGroup);
				    //crossImage = (ImageView)userView.findViewById(R.id.crossImage);
				    RelativeLayout rl = (RelativeLayout)userView.findViewById(R.id.crossImageLayout);
				    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
				    userListView = (TextView)userView.findViewById(R.id.userListView);
				    userListView.setText("    Select one member\n	 Add expense for the member.\n");
					Button submitPres = (Button)userView.findViewById(R.id.submitPres);
					if(null!=cursor && cursor.moveToFirst())
					{
						submitPres.setVisibility(1);
						do{
							if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0)){
								RadioButton radioButton = new RadioButton(context);
								String name = cursor.getString(1);
								if(CommonConstant.STATUS_INACTIVE.equals(cursor.getString(6)))
								{
									radioButton.setEnabled(false);
									name = name + " (Deactivated)";
									radioButton.setTextColor(Color.RED);
								}
								
								radioButton.setText(name);
								radioButton.setId(cursor.getInt(0));
								userListRadioGroup.addView(radioButton);	
							}
							
						}while(cursor.moveToNext());
						
					}else{
						Toast toast = Toast.makeText(ViewGroupActivity.this, "No user added yet",Toast.LENGTH_SHORT);
						toast.show();
						submitPres.setVisibility(-1);
					}
					
					splitShareDB.closeDBConnection(cursor);
					
					submitPres.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
							if(userListRadioGroup.getCheckedRadioButtonId() == -1)
							{
								String msg = "Please select a user";
								Toast toast = Toast.makeText(ViewGroupActivity.this, msg,Toast.LENGTH_SHORT);
								toast.show();
							}else{
								int userId =0;
								String userName="";
								try{
									userId = ((RadioButton)userView.findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
									userName = String.valueOf(((RadioButton)userView.findViewById(userListRadioGroup.getCheckedRadioButtonId())).getText());
									popupWindow.dismiss();
									Intent intent = new Intent(context, AddExpenseActivity.class);
									intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
									intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
									intent.putExtra(CommonConstant.TRANSFER_USER_ID, userId);
									intent.putExtra(CommonConstant.TRANSFER_USER_NAME, userName);
									startActivity(intent);	
								}catch(Exception e)
								{
									Toast toast = Toast.makeText(ViewGroupActivity.this, CommonConstant.GENERIC_ERROR +" in adding "+userName,Toast.LENGTH_LONG);
									toast.show();
									e.printStackTrace();
								}
								
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
		
		
		//------- Update expense history ----------//
		updateExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(context, ExpenseHistoryActivity.class);
					intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
					intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
					startActivity(intent);
				
			}
		});
		
		//------- Get expense ----------//
		getExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				cursor = splitShareDB.getUserListByGroup(selectedGroupId);
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
			    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
				userView = layoutInflater.inflate(R.layout.userlist_layout, null);
			    final PopupWindow popupWindow = new PopupWindow(userView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    popupWindow.setTouchable(true);
		        popupWindow.setFocusable(true);  
			    popupWindow.showAtLocation(userView, Gravity.CENTER, 2,10);
			    userListRadioGroup  = (RadioGroup)userView.findViewById(R.id.userListRadioGroup);
			    RelativeLayout rl = (RelativeLayout)userView.findViewById(R.id.crossImageLayout);
			    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
			    TextView userListView = (TextView)userView.findViewById(R.id.userListView);
			    boolean flag = false;
				Button submitPres = (Button)userView.findViewById(R.id.submitPres);
				submitPres.setText("GO");
				if(null!=cursor && cursor.moveToFirst())
				{
					do{
						if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0))
						{
							RadioButton radioButton = new RadioButton(context);
							String name = cursor.getString(1);
							radioButton.setText(name);
							radioButton.setId(cursor.getInt(0));
							userListRadioGroup.addView(radioButton);
							
							flag = true;
							
						}
						
						
						
					}while(cursor.moveToNext());
					
					if(!flag)
					{
						userListView.setText("\t\tNo user is available for this group");
						submitPres.setVisibility(View.INVISIBLE);
					}
				}
				
				splitShareDB.closeDBConnection(cursor);
				submitPres.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						System.out.println("id:: "+userListRadioGroup.getCheckedRadioButtonId());
						if(userListRadioGroup.getCheckedRadioButtonId() == -1)
						{
							String msg = "Please select a user";
							Toast toast = Toast.makeText(ViewGroupActivity.this, msg,Toast.LENGTH_LONG);
							toast.show();
						}else{
							int userId =0;
							String userName="";
							try{
								userId = ((RadioButton)userView.findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();
								userName = String.valueOf(((RadioButton)userView.findViewById(userListRadioGroup.getCheckedRadioButtonId())).getText());
								
								Toast toast = Toast.makeText(ViewGroupActivity.this,  " Data for "+userName,Toast.LENGTH_SHORT);
								toast.show();
								popupWindow.dismiss();
								
								Intent intent = new Intent(context, ViewMemberExpense.class);
								intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
								intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
								intent.putExtra(CommonConstant.TRANSFER_USER_ID, userId);
								intent.putExtra(CommonConstant.TRANSFER_USER_NAME, userName);
								startActivity(intent);	
								
							}catch(Exception e)
							{
								Toast toast = Toast.makeText(ViewGroupActivity.this, CommonConstant.GENERIC_ERROR +" in adding "+userName,Toast.LENGTH_LONG);
								toast.show();
								e.printStackTrace();
							}
							
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
				
		
		//----------Delete Group 
		
		deleteGroup.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
							AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
							dlgAlert.setMessage(CommonConstant.DELETE_MSG);
							dlgAlert.setTitle(CommonConstant.DELETE_TITLE);
							dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					    	    public void onClick(DialogInterface dialog, int which) {			      	
					    	    	splitShareDB.deleteAllGroupInformation(selectedGroupId);
									Toast toast = Toast.makeText(ViewGroupActivity.this, "Group deleted successfully",Toast.LENGTH_SHORT);
									toast.show();
									Intent intent = new Intent(context, AdminActivity.class);
									//intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, -1);
									intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
									startActivity(intent);	
					    	    }
					    	});
							dlgAlert.setNegativeButton("NO", null);
							dlgAlert.setCancelable(true);
							dlgAlert.create().show();
						
					}
				});
		
		//--------groupUpdate
		groupUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				System.out.println("inside groupUpdate for "+selectedGroupId);
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
			    		.getSystemService(LAYOUT_INFLATER_SERVICE);  
				groupUpdateView = layoutInflater.inflate(R.layout.groupupdate_layout, null);
			    final PopupWindow popupWindow = new PopupWindow(groupUpdateView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    popupWindow.setTouchable(true);
		        popupWindow.setFocusable(true);
			    popupWindow.showAtLocation(groupUpdateView, Gravity.CENTER, 2,10);
			    //crossImage = (ImageView)groupUpdateView.findViewById(R.id.crossImage);
			    RelativeLayout rl = (RelativeLayout)groupUpdateView.findViewById(R.id.crossImageLayout);
			    crossImage = (ImageView)rl.findViewById(R.id.crossImage);
			    groupDescription = (EditText)groupUpdateView.findViewById(R.id.groupDescription);
			    groupEstimatedCost  = (EditText)groupUpdateView.findViewById(R.id.groupEstimatedCost);
			    groupDescription.setText(groupDescriptionText);
			    groupEstimatedCost.setText(groupCostText);
			    Button submit = (Button)groupUpdateView.findViewById(R.id.submit);
			    submit.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						if(("".equals(groupDescription.getText().toString()))
								|| ("".equals(groupEstimatedCost.getText().toString())))
						{
							Toast toast = Toast.makeText(ViewGroupActivity.this, "Please add details",Toast.LENGTH_SHORT);
							toast.show();
						}else{
							GroupVO groupVO = new GroupVO();
							groupVO.setGroupId(selectedGroupId);
							groupVO.setGroupDescription(groupDescription.getText().toString());
							groupVO.setEstimatedCost(Integer.parseInt(groupEstimatedCost.getText().toString()));
							splitShareDB.updateGroupDescrition(groupVO);
							Toast toast = Toast.makeText(ViewGroupActivity.this, "Group updated successfully",Toast.LENGTH_SHORT);
							toast.show();
							Intent intent = new Intent(context, ViewGroupActivity.class);
							intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, selectedGroupId);
							intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
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
	}

	private void getGroupMember(int selectedGroupId2) {
		// TODO Auto-generated method stub
		
	}

	private void getAllGroupList() {/*
		
		cursor = splitShareDB.getAllGroupList(CommonConstant.STATUS_ACTIVE);
		if(null!=cursor && cursor.moveToFirst())
		{
			addMember.setEnabled(true);
			addExpense.setEnabled(true);
			getReport.setEnabled(true);
			deleteGroup.setEnabled(true);
			getExpense.setEnabled(true);
			addMember.setVisibility(1);
			addExpense.setVisibility(1);
			getReport.setVisibility(1);
			deleteGroup.setVisibility(1);
			getExpense.setVisibility(1);
			do{
				RadioButton radioButton = new RadioButton(this);
				radioButton.setId(cursor.getInt(0));
				radioButton.setText(cursor.getString(1));
				if(currentGroupId == cursor.getInt(0))
					radioButton.setChecked(true);
				groupListRadioGroup.addView(radioButton);
			}while(cursor.moveToNext());
			splitShareDB.closeDBConnection(cursor);
		}else{
			addMember.setEnabled(false);
			addExpense.setEnabled(false);
			getReport.setEnabled(false);
			deleteGroup.setEnabled(false);
			getExpense.setEnabled(false);
			addMember.setVisibility(-1);
			addExpense.setVisibility(-1);
			getReport.setVisibility(-1);
			deleteGroup.setVisibility(-1);
			getExpense.setVisibility(-1);
			Toast toast = Toast.makeText(ViewGroupActivity.this, "No group is added yet", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	*/}

	public void getGroupDetail(int currentGroupId) {
		try{
			groupDetail.setVisibility(1);
			groupUpdate.setVisibility(1);
			//groupUpdate.setTextColor(Color.BLUE);
			String groupDetailtext = "";
			cursor = splitShareDB.getGroupDetails(currentGroupId);
			if(null!=cursor && cursor.moveToFirst())
			{
				do{
				groupDetailtext = groupDetailtext +"Group Name: "+ cursor.getString(1) +"\n"+
								"Group Description: "+ cursor.getString(2) +"\n"+
								"Created By: "+ cursor.getString(4) +"\n"+
								"Created on: "+ cursor.getString(3) +"\n";
				groupDescriptionText = cursor.getString(2) ;
				groupCostText = cursor.getString(5) ;
				selectedGroupName = cursor.getString(1);
				}while(cursor.moveToNext());
			}
			groupDetail.setText(groupDetailtext);	
			
		}catch (Exception e) {
			Toast toast = Toast.makeText(ViewGroupActivity.this, CommonConstant.GENERIC_ERROR,Toast.LENGTH_SHORT);
			toast.show();
		}finally{
			splitShareDB.closeDBConnection(cursor);
		}
		
	}
	
	View.OnClickListener getOnClickAddUser(final CheckBox checkBox)
	{ 
		return new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				int userId =  checkBox.getId();
				if(userMap.containsKey(userId))
				{
					if(checkBox.isChecked())
					{
						//product.add(checkBox.getId());
					}
					if(!checkBox.isChecked())
					{
						userMap.remove(userId);
					}
				}
				else
				{
					userMap.put(userId, checkBox.getText().toString());
				}
				System.out.println("userMap:: "+userMap);
				} 
			};
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group, menu);
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
