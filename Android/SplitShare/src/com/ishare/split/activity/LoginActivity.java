
package com.ishare.split.activity;


import java.io.File;
import java.util.Calendar;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;
import com.ishare.split.vo.ExpenseVO;
import com.ishare.split.vo.UserSessionVO;
import com.ishare.split.vo.UserVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	TextView loginName;
	Button loginButton;
	SplitShareDB splitShareDB;
	String dbName;
	UserSessionVO userSessionVO;
	Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Context context = this;
        dbName = SplitShareDB.DATABASE_NAME;
        splitShareDB = new SplitShareDB(this);
        try{
        //Checling if DB exists
        if (!checkDataBase(this, dbName)) {
			Log.d(CommonConstant.APPLICATION_NAME, "coming here to create new DB");
			performInitDB(); // perform DB loading 
			
		}else{
			
			Log.d(CommonConstant.APPLICATION_NAME, "DB Exists");
			
		}
        loginButton = (Button)findViewById(R.id.loginButton);
        loginName = (TextView)findViewById(R.id.loginName);
        userSessionVO = new UserSessionVO();
        cursor = splitShareDB.getUserSession();
        if(null!=cursor && cursor.moveToFirst())
        {
        	userSessionVO.setSessionUserId(cursor.getInt(0));
        	userSessionVO.setSessionUserId(CommonConstant.USER_ADMIN_ID);
        	Intent adminIntent = new Intent(context, AdminActivity.class);
			adminIntent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
        	startActivity(adminIntent);
        }else{
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//if(CommonConstant.USER_ADMIN_CODE.equalsIgnoreCase(loginName.getText().toString()))
				if(!"".equalsIgnoreCase(loginName.getText().toString().trim()))
		        {
					
					//-populating session details for admin
					userSessionVO.setSessionUserId(CommonConstant.USER_ADMIN_ID);
					userSessionVO.setSessionCreateDate(DateUtil.getDateFormat(
							Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
					userSessionVO.setName(loginName.getText().toString().trim());
					splitShareDB.insertSession(userSessionVO);
					
					//perform DB task
					performDBTask(context);
		        	
					Intent adminIntent = new Intent(context, AdminActivity.class);
					adminIntent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
		        	startActivity(adminIntent);
		        }else{
		        	Toast toast = Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT);
		        	toast.show();
		        }
				
				
			}

			
		});
        }
       }catch(Exception e)
       {
    	   e.printStackTrace();
       }
    }


    
 private void performInitDB() throws Exception{
	 Log.d(CommonConstant.APPLICATION_NAME, "Entered into performInitDB");
	 try
	 {
		
		 
		 //-------INSERT SEQUENCE ---------//
		 insertSequence();
		 
		 //------ INSERT INTO USER_TABLE ----------//
		 insertAdminUser(); 
		 
		 //------ INSERT INTO USER_TABLE ----------//
		 insertUser();
		 
		 //------ INSERT INTO EXPENSE TABLE ----------//
		// insertExpense();
		 
	 }catch(Exception e){
		 Log.d(CommonConstant.APPLICATION_NAME, e.getMessage());
		 throw new Exception();
	 }
		
	}



	



	


	private void insertExpense() {
		int expenseId = splitShareDB.getMaxExpenseSeq();
		ExpenseVO expenseVO = new ExpenseVO();
		expenseVO.setExpenseId(expenseId);
		expenseVO.setGroupId(101);
		expenseVO.setUserId(101);
		expenseVO.setExpenseDescription("hotel advance");
		expenseVO.setReceiverId(0);
		expenseVO.setCreateId(101);
		expenseVO.setAmount(3000);
		expenseVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		expenseVO.setStatus(CommonConstant.EXPENSE_STATUS_EXPENSE);
		splitShareDB.insertExpense(expenseVO);
		splitShareDB.updateExpenseSeq(expenseId);
		
		expenseId = splitShareDB.getMaxExpenseSeq();
		expenseVO = new ExpenseVO();
		expenseVO.setExpenseId(expenseId);
		expenseVO.setGroupId(101);
		expenseVO.setUserId(101);
		expenseVO.setExpenseDescription("advance to sanchari");
		expenseVO.setReceiverId(102);
		expenseVO.setCreateId(101);
		expenseVO.setAmount(1000);
		expenseVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		expenseVO.setStatus(CommonConstant.EXPENSE_STATUS_ADVANCE);
		splitShareDB.insertExpense(expenseVO);
		splitShareDB.updateExpenseSeq(expenseId);
		
		expenseId = splitShareDB.getMaxExpenseSeq();
		expenseVO = new ExpenseVO();
		expenseVO.setExpenseId(expenseId);
		expenseVO.setGroupId(101);
		expenseVO.setUserId(102);
		expenseVO.setExpenseDescription("hotel advance");
		expenseVO.setReceiverId(0);
		expenseVO.setCreateId(102);
		expenseVO.setAmount(7000);
		expenseVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		expenseVO.setStatus(CommonConstant.EXPENSE_STATUS_EXPENSE);
		splitShareDB.insertExpense(expenseVO);
		splitShareDB.updateExpenseSeq(expenseId);
		
		System.out.println("expenseId:: "+expenseId);
	
}



	private void insertSequence() {
		splitShareDB.insertGroupSeq(CommonConstant.DEFAULT_SEQUENCE);
		splitShareDB.insertUserSeq(CommonConstant.DEFAULT_SEQUENCE);
		splitShareDB.insertExpenseSeq(CommonConstant.DEFAULT_SEQUENCE);
	
}

	private void insertAdminUser() {
		UserVO userVO = new UserVO();
		//-------Admin --------//
		userVO.setUserId(CommonConstant.USER_ADMIN_ID);
		userVO.setRoleId(CommonConstant.USER_ADMIN_ROLEID);
		userVO.setCode(CommonConstant.USER_ADMIN_CODE);
		userVO.setName(CommonConstant.USER_ADMIN_NAME);
		userVO.setEmail(CommonConstant.DEFAULT_NA);
		userVO.setPhone(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactName(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactPhone(CommonConstant.DEFAULT_NA);
		userVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		userVO.setStatus(CommonConstant.STATUS_ACTIVE);
		splitShareDB.insertUser(userVO);
		
	}


	public void insertUser() {
	
		int userId = 0;
		UserVO userVO = new UserVO();
		//-------- Test User 1-----------//
		userId = splitShareDB.getMaxUserSeq();
		userVO.setUserId(userId);
		userVO.setRoleId(CommonConstant.USER_USER_ROLEID);
		userVO.setCode(CommonConstant.USER_USER_CODE+userId);
		userVO.setName("Prasenjit");
		userVO.setEmail("m.senjit@gmail.com");
		userVO.setPhone(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactName(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactPhone(CommonConstant.DEFAULT_NA);
		userVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		userVO.setStatus(CommonConstant.STATUS_ACTIVE);
		splitShareDB.insertUser(userVO);
		splitShareDB.updateUserSeq(userId);
		//-------- Test User 2-----------//
		userId = splitShareDB.getMaxUserSeq();
		userVO.setUserId(userId);
		userVO.setRoleId(CommonConstant.USER_USER_ROLEID);
		userVO.setCode(CommonConstant.USER_USER_CODE+userId);
		userVO.setName("Sanchari");
		userVO.setEmail("sanbi.1508@gmail.com");
		userVO.setPhone(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactName(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactPhone(CommonConstant.DEFAULT_NA);
		userVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		userVO.setStatus(CommonConstant.STATUS_ACTIVE);
		splitShareDB.insertUser(userVO);
		splitShareDB.updateUserSeq(userId);
		//-------- Test User 3-----------//
		userId = splitShareDB.getMaxUserSeq();
		userVO.setUserId(userId);
		userVO.setRoleId(CommonConstant.USER_USER_ROLEID);
		userVO.setCode(CommonConstant.USER_USER_CODE+userId);
		userVO.setName("Saswata");
		userVO.setEmail(CommonConstant.DEFAULT_NA);
		userVO.setPhone(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactName(CommonConstant.DEFAULT_NA);
		userVO.setEmergencyContactPhone(CommonConstant.DEFAULT_NA);
		userVO.setCreateDate(DateUtil.getDateFormat(Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
		userVO.setStatus(CommonConstant.STATUS_ACTIVE);
		splitShareDB.insertUser(userVO);
		splitShareDB.updateUserSeq(userId);
}



	// checking if the DB exists
 	public boolean checkDataBase(Context context, String dbName)
 	{
 		boolean dbExists = false;
 		SQLiteDatabase checkDB = null;
 
 		try {

 			File dbFile = context.getDatabasePath(dbName);
 			if (dbFile.exists()) {
 				Log.d(CommonConstant.APPLICATION_NAME,"path "+dbFile.getAbsolutePath());
 				dbExists = true;
 				//dbExists = deleteDB(dbFile);
 				
 			}

 		} catch (SQLiteException e) {

 			dbExists = false;
 			e.printStackTrace();

 		}

 		return dbExists;

 	}



	private boolean deleteDB(File dbFile) {
		boolean dbExists;
		System.out.println("Forcefully deleteing DB");
		dbFile.delete();
		dbExists = false;
		System.out.println("DB deleted");
		return dbExists;
	}
	
	
	private void performDBTask(final Context context) {
		
		System.out.println("inside perform DB"); 
		Cursor result = splitShareDB.testDBActivity();
		if(null!=result && result.moveToFirst())
		{
			do{
				System.out.println(">>>"+result.getDouble(0));
			}while(result.moveToNext());
		}
		Toast toast = Toast.makeText(context, "table created", Toast.LENGTH_SHORT);
		toast.show();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	moveTaskToBack(true); 
    }
}
