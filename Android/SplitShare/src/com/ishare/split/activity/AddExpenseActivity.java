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
import com.ishare.split.vo.UserGroupVO;
import com.ishare.split.vo.UserSessionVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddExpenseActivity extends BaseActivity {

	UserGroupVO userGroupVO;
	Cursor cursor;
	ImageView homeImage;
	int userId;
	int groupId;
	String name;
	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button submit;
	TextView description;
	TextView amount;
	CheckBox advPymtChk;
	RadioGroup userListRadioGroup;
	int advPymtUserId;
	ExpenseVO expenseVO;
	ExpenseUserVO expenseUserVO;
	boolean chk;
	String errorMsg = ""; 
	int expenseId;
	int userExpenseId;
	String pymtAdvStatus;
	boolean flag = false;
	TextView headerMsg;
	LinearLayout listLinearLayOut;
	TextView userExpenseAddTextView;
	Map<Integer, String> userMap = new HashMap<Integer, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		groupId = savedInstanceState.getInt(CommonConstant.TRANSFER_GROUP_ID);
		userId = savedInstanceState.getInt(CommonConstant.TRANSFER_USER_ID);
		name = savedInstanceState.getString(CommonConstant.TRANSFER_USER_NAME);
		description = (TextView)findViewById(R.id.description);
		amount =  (TextView)findViewById(R.id.amount);
		userListRadioGroup = (RadioGroup)findViewById(R.id.userListRadioGroup);
		advPymtChk = (CheckBox)findViewById(R.id.advPymtChk);
		submit = (Button)findViewById(R.id.submit);
		headerMsg = (TextView)findViewById(R.id.headerMsg);
		headerMsg.setText("  Enter expense details for "+name);
		listLinearLayOut = (LinearLayout)findViewById(R.id.listLinearLayOut);
		userExpenseAddTextView = (TextView)findViewById(R.id.userExpenseAddTextView);
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
		getUserList();
		if(!flag)
		{
			advPymtChk.setVisibility(View.GONE);
		}	
		else
			advPymtChk.setVisibility(1);
		
		chk = advPymtChk.isChecked();
		advPymtChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean chk) {
				if(chk)
				 {
					userListRadioGroup.setVisibility(1);
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
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
					  addExpense(context);
					}
				  else{
					  Toast toast = Toast.makeText(AddExpenseActivity.this, errorMsg, Toast.LENGTH_SHORT);
					  toast.show();
				  }
				  
			  }catch(Exception e)
			  {
				  Toast toast = Toast.makeText(AddExpenseActivity.this, CommonConstant.GENERIC_ERROR, Toast.LENGTH_SHORT);
				  toast.show();
				  e.printStackTrace();
				  
			  }finally{
				  splitShareDB.closeDBConnection(cursor);
			  }
				
			}
		});
		
		
	}
	
	private void addExpense(final Context context){
		

	    if(advPymtChk.isChecked())
	    {
	    	advPymtUserId = ((RadioButton)findViewById(userListRadioGroup.getCheckedRadioButtonId())).getId();	
	    	pymtAdvStatus = CommonConstant.EXPENSE_STATUS_ADVANCE;
	    }else{
	    	advPymtUserId = CommonConstant.ZERO;
	    	pymtAdvStatus = CommonConstant.EXPENSE_STATUS_EXPENSE;
	    }
	  	expenseId = splitShareDB.getMaxExpenseSeq();
		expenseVO = new ExpenseVO();
		expenseVO.setExpenseId(expenseId);
		expenseVO.setGroupId(groupId);
		expenseVO.setUserId(userId);
		expenseVO.setExpenseDescription(description.getText().toString());
		expenseVO.setReceiverId(advPymtUserId);
		expenseVO.setCreateId(userSessionVO.getSessionUserId());
		expenseVO.setAmount(Integer.parseInt(amount.getText().toString()));
		expenseVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		expenseVO.setStatus(pymtAdvStatus);
		splitShareDB.insertExpense(expenseVO);
		splitShareDB.updateExpenseSeq(expenseId);
		
		//
		if(!advPymtChk.isChecked())
		{
			addUserWiseExpense(context,expenseVO);
		}
		
		
		Toast toast = Toast.makeText(AddExpenseActivity.this, "Expense added for "+name, Toast.LENGTH_SHORT);
		toast.show();
		Intent intent = new Intent(context, ViewGroupActivity.class);
		intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
		intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, groupId);
		startActivity(intent);	
		
		
	
		
	}
	
	private void addUserWiseExpense(final Context context,ExpenseVO expenseVO){
		
		expenseUserVO = new ExpenseUserVO();
		expenseUserVO.setExpenseId(expenseVO.getExpenseId());
		
		Iterator<Integer> iterator = userMap.keySet().iterator();
		int userNumber = userMap.size();
		expenseUserVO.setAmount(expenseVO.getAmount()/userNumber);
		
		while(iterator.hasNext())
		{
			userExpenseId = splitShareDB.getMaxUserExpenseSeq();
			expenseUserVO.setUserExpenseId(userExpenseId);
			expenseUserVO.setUserId(iterator.next());
			expenseUserVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
			splitShareDB.insertUserExpense(expenseUserVO);
			splitShareDB.updateUserExpenseSeq(userExpenseId);
		}
		
		
	}

	private void getUserList() 
	{
		
		userListRadioGroup.setVisibility(View.GONE);
		//listLinearLayOut.setVisibility(View.GONE);
		
		cursor = splitShareDB.getUserListByGroup(groupId);
		
		//Adding user for advance list
		boolean userCheckedFlag = false;
		if(null!=cursor && cursor.moveToFirst())
		{
			//int counter = 0;
			do{
				if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0) && userId!=cursor.getInt(0) ){
					
					
					RadioButton radioButton = new RadioButton(this);
					radioButton.setText(cursor.getString(1));
					radioButton.setId(cursor.getInt(0));
					if(CommonConstant.STATUS_INACTIVE.equals(cursor.getString(6)))
						radioButton.setEnabled(false);
						
					//counter ++ ;
					userListRadioGroup.addView(radioButton);
					if(CommonConstant.STATUS_ACTIVE.equals(cursor.getString(6)))
					{
						flag = true;
						if(!userCheckedFlag)
						{
							radioButton.setChecked(true);
							userCheckedFlag = true;
						}
							
					}
					
				}
				
			}while(cursor.moveToNext());
		}
		
		//Adding user for expense selection list
		cursor = splitShareDB.getUserListByGroup(groupId);
		if(null!=cursor && cursor.moveToFirst())
		{
			//int counter = 0;
			do{
				if(CommonConstant.USER_ADMIN_ID!=cursor.getInt(0)){
					
					CheckBox userCheckbox = new CheckBox(this);
					userCheckbox.setText(cursor.getString(1));
					userCheckbox.setId(cursor.getInt(0));
					userCheckbox.setChecked(true);
					if(userId==cursor.getInt(0)){
						userCheckbox.setEnabled(false);
					}
					userMap.put(cursor.getInt(0), userCheckbox.getText().toString());
					listLinearLayOut.addView(userCheckbox);
					userCheckbox.setOnClickListener(getOnClickAddUser(userCheckbox));
						
				}
				
			}while(cursor.moveToNext());
		
		}
		splitShareDB.closeDBConnection(cursor);
		
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
		getMenuInflater().inflate(R.menu.add_expense, menu);
		return true;
	}

}
