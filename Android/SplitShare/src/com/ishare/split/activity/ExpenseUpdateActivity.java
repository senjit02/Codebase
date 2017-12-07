package com.ishare.split.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;
import com.ishare.split.vo.ExpenseUserVO;
import com.ishare.split.vo.ExpenseVO;
import com.ishare.split.vo.UserSessionVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ExpenseUpdateActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	ExpenseVO expenseVO;
	Button submit;
	ImageView homeImage;
	RadioGroup userListRadioGroup;
	int expenseId;
	int receiverID;
	int newReceiverID;
	EditText amount;
	EditText description;
	String status;
	String newStatus;
	CheckBox advCheck;
	Cursor cursor;
	int userId;
	boolean chk;
	String errorMsg = "";
	boolean hasGroupMember = false;
	boolean hasAnyActiveMember = false;
	LinearLayout listLinearLayOut;
	TextView userExpenseAddTextView;
	Map<Integer, String> userMap = new HashMap<Integer, String>();
	Map<Integer, ExpenseUserVO> expenseUserMap = new HashMap<Integer, ExpenseUserVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_update);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		expenseVO = (ExpenseVO) savedInstanceState.getSerializable(CommonConstant.TRANSFER_VO);
		splitShareDB = new SplitShareDB(this);
		
		initializeElements();
		
		performCommonTask(context);
		
		getExpenseDetail(expenseVO);
		
		viewListenerManager();
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try{
					errorMsg = "";
					if(("").equals(description.getText().toString()))
					  {
						  errorMsg = "Please enter expense description";
					  }
					if(("").equals(amount.getText().toString()))
					  {
						  errorMsg = "Please enter expense amount";
					  }
					
					if(("").equals(errorMsg))
					{
						updateExpenseDetails(context);
					}
					 else{
						  Toast toast = Toast.makeText(ExpenseUpdateActivity.this, errorMsg, Toast.LENGTH_SHORT);
						  toast.show();
					  }
					  
				}catch(Exception e)
				{
					e.printStackTrace();
					Toast toast = Toast.makeText(ExpenseUpdateActivity.this, CommonConstant.GENERIC_ERROR,Toast.LENGTH_SHORT);
					toast.show();
				}finally{
					splitShareDB.closeDBConnection(cursor);
				}
				
			}

			private void updateExpenseDetails(final Context context) {
				expenseVO.setExpenseDescription(description.getText().toString());
				expenseVO.setAmount(Integer.parseInt(amount.getText().toString()));
				 if(advCheck.isChecked())
				    {
					 	newReceiverID = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();	
					 	newStatus = CommonConstant.EXPENSE_STATUS_ADVANCE;
				    }else{
				    	newReceiverID = 0;
				    	newStatus = CommonConstant.EXPENSE_STATUS_EXPENSE;
				    }
				 
				 expenseVO.setReceiverId(newReceiverID);
				 expenseVO.setStatus(newStatus);
				 splitShareDB.updateExpense(expenseVO);
				 
				 
					if(!advCheck.isChecked())
					{
						updateUserWiseExpense(context,expenseVO);
					}
				 
				 Toast toast = Toast.makeText(ExpenseUpdateActivity.this, "Successfully Updated",Toast.LENGTH_SHORT);
				 toast.show(); 
				 Intent intent = new Intent(context, ExpenseHistoryActivity.class);
				 intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				 intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, expenseVO.getGroupId());
				 startActivity(intent);
			}

			
		});
	}

	private void viewListenerManager() {
		if(!hasAnyActiveMember)
		{
			Toast toast = Toast.makeText(ExpenseUpdateActivity.this, "No active member!Advance not possible",Toast.LENGTH_LONG);
			toast.show(); 
			userListRadioGroup.setEnabled(false);
			advCheck.setEnabled(false);
		}
		chk = advCheck.isChecked();
		advCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean chk) {
				if(chk)
				 {
					userListRadioGroup.setVisibility(View.VISIBLE);
					listLinearLayOut.setVisibility(View.GONE);
					userExpenseAddTextView.setVisibility(View.GONE);
						
				 }
				if(!chk)
				{
					userListRadioGroup.setVisibility(View.GONE);
					listLinearLayOut.setVisibility(View.VISIBLE);
					userExpenseAddTextView.setVisibility(View.VISIBLE);
					
				}
				
			}
		});
	}

	private void performCommonTask(final Context context) {
		homeImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommonConstant.ADMIN_ACTIVITY);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);	
				
			}
		});
	}

	private void initializeElements() {
		description = (EditText)findViewById(R.id.description);
		amount = (EditText)findViewById(R.id.amount);
		advCheck = (CheckBox)findViewById(R.id.advCheck);
		submit = (Button)findViewById(R.id.submit);
		userListRadioGroup = (RadioGroup)findViewById(R.id.userListRadioGroup);
		userListRadioGroup.setVisibility(View.GONE);
		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		homeImage= (ImageView)iconLayout.findViewById(R.id.homeImage);
		listLinearLayOut = (LinearLayout)findViewById(R.id.listLinearLayOut);
		userExpenseAddTextView = (TextView)findViewById(R.id.userExpenseAddTextView);
		homeImage = (ImageView)findViewById(R.id.homeImage);
	}

	private void getExpenseDetail(ExpenseVO expenseVO) {
		
		try{
			
			cursor = splitShareDB.getExpenseDetailByExpenseId(expenseVO);
			if(null!=cursor && cursor.moveToFirst())
			{
				do{
					description.setText(cursor.getString(4));
					amount.setText(cursor.getString(3));
					status = cursor.getString(8);
					userId = cursor.getInt(2);
					receiverID = cursor.getInt(5);
					getGroupMemberList(expenseVO.getGroupId());
					
					if(CommonConstant.EXPENSE_STATUS_ADVANCE.equals(status))
					{
						//advCheck.setVisibility(1);
						userListRadioGroup.setVisibility(1);
						advCheck.setChecked(true);
						listLinearLayOut.setVisibility(View.GONE);
						userExpenseAddTextView.setVisibility(View.GONE);
						
					}else{
						advCheck.setChecked(false);
						if(hasGroupMember)
						{
							advCheck.setVisibility(1);
						}else{
							advCheck.setVisibility(View.GONE);
						}
						userListRadioGroup.setVisibility(View.GONE);
						listLinearLayOut.setVisibility(View.VISIBLE);
						userExpenseAddTextView.setVisibility(View.VISIBLE);
					}
					
				}while(cursor.moveToNext());
			}
			//cursor = splitShareDB.getUserListByGroup(groupId);
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
			Toast toast = Toast.makeText(ExpenseUpdateActivity.this, CommonConstant.GENERIC_ERROR,Toast.LENGTH_SHORT);
			toast.show();
		}
		finally{
			splitShareDB.closeDBConnection(cursor);
		}
		
	}

	private void getGroupMemberList(int groupId) {
		try{
			
			cursor = splitShareDB.getUserListByGroup(groupId);
			if(null!=cursor && cursor.moveToFirst())
			{
				do{
					if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0) && userId!=cursor.getInt(0) ){
						
							hasGroupMember = true;
							RadioButton radioButton = new RadioButton(this);
							radioButton.setText(cursor.getString(1));
							radioButton.setId(cursor.getInt(0));
							if(receiverID == cursor.getInt(0))
								radioButton.setChecked(true);
							userListRadioGroup.addView(radioButton);	
							if(CommonConstant.STATUS_INACTIVE.equals(cursor.getString(6)))
							{
								radioButton.setEnabled(false);
								radioButton.setTextColor(Color.RED);
								radioButton.setText(cursor.getString(1) + " (Deactivated)");
							}else{
								hasAnyActiveMember = true;
							}
						
					}
					
				}while(cursor.moveToNext());
				/*if(hasGroupMember)
				{
					advCheck.setChecked(true);
				}else{
					
				}*/
			}
			
			//Adding user for expense selection list
			cursor = null;
			cursor = splitShareDB.getUserExpenseListByExpenseId(expenseVO.getExpenseId());
			//USEREXPENSEID INTEGER,EXPENSEID INTEGER,USERID INTEGER,AMOUNT INTEGER,CREATEDATE DATE
			if(null!=cursor && cursor.moveToFirst())
			{
				do{
					ExpenseUserVO expenseUserVO = new ExpenseUserVO();
					expenseUserVO.setUserExpenseId(cursor.getInt(0));
					expenseUserVO.setExpenseId(cursor.getInt(1));
					expenseUserVO.setUserId(cursor.getInt(2));
					expenseUserVO.setAmount(cursor.getInt(3));
					
					expenseUserMap.put(cursor.getInt(2), expenseUserVO);
				}while(cursor.moveToNext());
			}
			
			cursor = null;
			cursor = splitShareDB.getUserListByGroup(groupId);
			if(null!=cursor && cursor.moveToFirst())
			{
				//int counter = 0;
				do{
					if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0)){
						
						CheckBox userCheckbox = new CheckBox(this);
						userCheckbox.setText(cursor.getString(1));
						userCheckbox.setId(cursor.getInt(0));
						if(expenseUserMap.containsKey(cursor.getInt(0))){
							userCheckbox.setChecked(true);
							userMap.put(cursor.getInt(0), userCheckbox.getText().toString());
						}else
							userCheckbox.setChecked(false);
						
						if(userId==cursor.getInt(0)){
							userCheckbox.setChecked(true);
							userCheckbox.setEnabled(false);
						}
						
						listLinearLayOut.addView(userCheckbox);
						userCheckbox.setOnClickListener(getOnClickAddUser(userCheckbox));
							
					}
					
				}while(cursor.moveToNext());
			
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}
		finally{
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
	
	
	private void updateUserWiseExpense(Context context, ExpenseVO expenseVO) {

		
		ExpenseUserVO expenseUserVO = new ExpenseUserVO();
		expenseUserVO.setExpenseId(expenseVO.getExpenseId());
		
		splitShareDB.deleteUserExpenseById(expenseVO.getExpenseId());
		
		Iterator<Integer> iterator = userMap.keySet().iterator();
		int userNumber = userMap.size();
		expenseUserVO.setAmount(expenseVO.getAmount()/userNumber);
		
		while(iterator.hasNext())
		{
			int userExpenseId = splitShareDB.getMaxUserExpenseSeq();
			expenseUserVO.setUserExpenseId(userExpenseId);
			expenseUserVO.setUserId(iterator.next());
			expenseUserVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
			splitShareDB.insertUserExpense(expenseUserVO);
			splitShareDB.updateUserExpenseSeq(userExpenseId);
		}
		
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_update, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	//	super.onBackPressed();
		 Intent intent = new Intent(getBaseContext(), ExpenseHistoryActivity.class);
		 intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
		 intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, expenseVO.getGroupId());
		 startActivity(intent);
		
	}
}
