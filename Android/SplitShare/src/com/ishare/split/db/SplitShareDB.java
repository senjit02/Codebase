package com.ishare.split.db;

import com.ishare.split.util.SQLUtil;
import com.ishare.split.vo.ExpenseUserVO;
import com.ishare.split.vo.ExpenseVO;
import com.ishare.split.vo.GroupVO;
import com.ishare.split.vo.UserGroupVO;
import com.ishare.split.vo.UserSessionVO;
import com.ishare.split.vo.UserVO;
import com.ishare.split.constant.CommonConstant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SplitShareDB extends SQLiteOpenHelper{
	
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "splitShareDB";
	
	public static final String TABLE_CREATE_GROUP = SQLUtil.TABLE_CREATE_GROUP;
	public static final String TABLE_CREATE_USERGROUP_SEQUENCE = SQLUtil.TABLE_CREATE_GROUP_SEQUENCE;
	public static final String TABLE_CREATE_USER = SQLUtil.TABLE_CREATE_USER;
	public static final String TABLE_CREATE_USER_SEQUENCE = SQLUtil.TABLE_CREATE_USER_SEQUENCE;
	public static final String TABLE_CREATE_USER_SESSION = SQLUtil.TABLE_CREATE_USER_SESSION;
	public static final String TABLE_CREATE_GROUP_MEMBER = SQLUtil.TABLE_CREATE_GROUP_MEMBER;
	public static final String TABLE_CREATE_EXPENSE = SQLUtil.TABLE_CREATE_EXPENSE;
	public static final String TABLE_CREATE_EXPENSE_SEQUENCE = SQLUtil.TABLE_CREATE_EXPENSE_SEQUENCE;
	public static final String TABLE_CREATE_USER_EXPENSE = SQLUtil.TABLE_CREATE_USER_EXPENSE;
	public static final String TABLE_CREATE_USER_EXPENSE_SEQUENCE = SQLUtil.TABLE_CREATE_USER_EXPENSE_SEQUENCE;
	
	public static final String DATABASE_TABLE_GROUP = "USERGROUP";
	public static final String DATABASE_TABLE_USERGROUPSEQUENCE = "USERGROUP_SEQUENCE";
	public static final String DATABASE_TABLE_USER = "USER";
	public static final String DATABASE_TABLE_USERSEQUENCE = "USER_SEQUENCE";
	public static final String DATABASE_TABLE_USER_SESSION = "USER_SESSION";
	public static final String DATABASE_TABLE_GROUP_MEMBER = "GROUP_MEMBER";
	public static final String DATABASE_TABLE_EXPENSE = "EXPENSE";
	public static final String DATABASE_TABLE_USER_EXPENSE = "USER_EXPENSE";
	public static final String DATABASE_TABLE_EXPENSE_SEQUENCE = "EXPENSE_SEQUENCE";
	public static final String DATABASE_TABLE_USER_EXPENSE_SEQUENCE = "USER_EXPENSE_SEQUENCE";
	
	//--------GROUP
	public static final String KEY_GROUPID = "GROUPID";
	public static final String KEY_GROUPNAME = "GROUPNAME";
	public static final String KEY_DESCRIPTION = "DESCRIPTION";
	public static final String KEY_CREATEID = "CREATEID";
	public static final String KEY_ESTIMATEDCOST = "ESTIMATEDCOST";
		
	//--------USER
	public static final String KEY_USERID = "USERID";
	public static final String KEY_NAME = "NAME";
	public static final String KEY_USERCODE = "USERCODE";
	public static final String KEY_ROLEID = "ROLEID";
	public static final String KEY_EMAIL = "EMAIL";
	public static final String KEY_PHONE = "PHONE";
	public static final String KEY_EMERGENCYNAME = "EMERGENCYNAME";
	public static final String KEY_EMERGENCYNUMBER = "EMERGENCYNUMBER";
	public static final String KEY_CREATEDATE = "CREATEDATE";
	public static final String KEY_STATUS = "STATUS";
	
	//------USERGROUP
	public static final String KEY_GROUPROLEID = "GROUPROLEID";
	
	//------EXPENSE
	public static final String KEY_EXPENSEID = "EXPENSEID";
	public static final String KEY_RECEIVERID = "RECEIVERID";
	public static final String KEY_AMOUNT = "AMOUNT";
	
	//----- USER EXPENSE
	public static final String KEY_USEREXPENSEID = "USEREXPENSEID";

	//-------SEQUENCE
	public static final String KEY_SEQUENCE = "SEQUENCE";
	
	public SplitShareDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
			db.execSQL(TABLE_CREATE_USER);
			db.execSQL(TABLE_CREATE_GROUP);
			db.execSQL(TABLE_CREATE_USERGROUP_SEQUENCE);
			db.execSQL(TABLE_CREATE_USER);
			db.execSQL(TABLE_CREATE_USER_SEQUENCE);
			db.execSQL(TABLE_CREATE_USER_SESSION);
			db.execSQL(TABLE_CREATE_GROUP_MEMBER);
			db.execSQL(TABLE_CREATE_EXPENSE);
			db.execSQL(TABLE_CREATE_EXPENSE_SEQUENCE);
			db.execSQL(TABLE_CREATE_USER_EXPENSE);
			db.execSQL(TABLE_CREATE_USER_EXPENSE_SEQUENCE);
			Log.d(CommonConstant.APPLICATION_NAME, "Tables created succesfully");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public void insertUserSeq(int userId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SEQUENCE,userId+1);
		getWritableDatabase().insert(DATABASE_TABLE_USERSEQUENCE, null, initialValues);
	}
	
	public int getMaxUserSeq() {
		 Cursor cr = getReadableDatabase().rawQuery(SQLUtil.GET_MAX_USER_SEQUENCE, null);
		 cr.moveToFirst();
		 return cr.getInt(0);
	}
	
	public boolean updateUserSeq(int userId) {
		ContentValues args = new ContentValues();
		int newSeq = userId+1;
		args.put(KEY_SEQUENCE,newSeq);		
		return getWritableDatabase().update(DATABASE_TABLE_USERSEQUENCE, args,null, null) > 0;
		
	}
	
	public void insertUser(UserVO userVO) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID, userVO.getUserId());
		initialValues.put(KEY_NAME, userVO.getName());
		initialValues.put(KEY_USERCODE, userVO.getCode());
		initialValues.put(KEY_ROLEID, userVO.getRoleId());
		initialValues.put(KEY_EMAIL, userVO.getEmail());
		initialValues.put(KEY_PHONE, userVO.getPhone());
		initialValues.put(KEY_EMERGENCYNAME,userVO.getEmergencyContactName());
		initialValues.put(KEY_EMERGENCYNUMBER, userVO.getEmergencyContactPhone());
		initialValues.put(KEY_CREATEDATE, userVO.getCreateDate());
		initialValues.put(KEY_STATUS, userVO.getStatus());
		getWritableDatabase().insert(DATABASE_TABLE_USER, null, initialValues);
	}
	

	

	public void addGroup(GroupVO groupVO) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GROUPID, groupVO.getGroupId());
		initialValues.put(KEY_GROUPNAME, groupVO.getGroupName());
		initialValues.put(KEY_DESCRIPTION, groupVO.getGroupDescription());
		initialValues.put(KEY_CREATEID,groupVO.getCreateId());
		initialValues.put(KEY_CREATEDATE, groupVO.getCreateDate());
		initialValues.put(KEY_STATUS, groupVO.getStatus());
		initialValues.put(KEY_ESTIMATEDCOST, groupVO.getEstimatedCost());
		getWritableDatabase().insert(DATABASE_TABLE_GROUP, null, initialValues);
		
	}
	
	public void insertGroupSeq(int groupId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SEQUENCE,groupId+1);
		getWritableDatabase().insert(DATABASE_TABLE_USERGROUPSEQUENCE, null, initialValues);
	}
	public int getMaxGroupSeq() {
		 Cursor cr = getReadableDatabase().rawQuery(SQLUtil.GET_MAX_GROUP_SEQUENCE, null);
		 cr.moveToFirst();
		 return cr.getInt(0);
	}
	public boolean updateGroupSeq(int groupId) {
		ContentValues args = new ContentValues();
		int newSeq = groupId+1;
		args.put(KEY_SEQUENCE,newSeq);		
		return getWritableDatabase().update(DATABASE_TABLE_USERGROUPSEQUENCE, args,null, null) > 0;
		
	}

	public Cursor getUserListToAdd(int selectedGroupId) {
		String query = SQLUtil.GET_USER_LIST_TO_ADD;
		query = query.replace("@id", String.valueOf(selectedGroupId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	
	public void closeDBConnection(Cursor cursor) {
		if(null!=cursor)
			cursor.close();
	}

	public Cursor getAllGroupList(String status) {
		String query = SQLUtil.GET_GROUP_LIST;
		query = query.replace("@status", status);
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void insertGroupMember(UserGroupVO userGroupVO) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID, userGroupVO.getUserId());
		initialValues.put(KEY_GROUPID, userGroupVO.getGroupId());
		initialValues.put(KEY_GROUPROLEID, userGroupVO.getGroupRoleId());
		initialValues.put(KEY_CREATEDATE, userGroupVO.getCreateDate());
		initialValues.put(KEY_CREATEID, userGroupVO.getCreateId());
		initialValues.put(KEY_STATUS, userGroupVO.getStatus());
		getWritableDatabase().insert(DATABASE_TABLE_GROUP_MEMBER, null, initialValues);
		
	}

	public void insertExpenseSeq(int expenseId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SEQUENCE,expenseId+1);
		getWritableDatabase().insert(DATABASE_TABLE_EXPENSE_SEQUENCE, null, initialValues);
	}

	public int getMaxExpenseSeq() {
		 Cursor cr = getReadableDatabase().rawQuery(SQLUtil.GET_MAX_EXEPENSE_SEQUENCE, null);
		 cr.moveToFirst();
		 return cr.getInt(0);
	}
	public int getMaxUserExpenseSeq() {
		 Cursor cr = getReadableDatabase().rawQuery(SQLUtil.GET_MAX_USER_EXEPENSE_SEQUENCE, null);
		 cr.moveToFirst();
		 return cr.getInt(0);
	}
	public boolean updateExpenseSeq(int expenseId) {
		ContentValues args = new ContentValues();
		int newSeq = expenseId+1;
		args.put(KEY_SEQUENCE,newSeq);		
		return getWritableDatabase().update(DATABASE_TABLE_EXPENSE_SEQUENCE, args,null, null) > 0;
		
	}
	
	public boolean updateUserExpenseSeq(int userExpenseId) {
		ContentValues args = new ContentValues();
		int newSeq = userExpenseId+1;
		args.put(KEY_SEQUENCE,newSeq);		
		return getWritableDatabase().update(DATABASE_TABLE_USER_EXPENSE_SEQUENCE, args,null, null) > 0;
		
	}

	public void insertExpense(ExpenseVO expenseVO) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EXPENSEID,expenseVO.getExpenseId());
		initialValues.put(KEY_USERID,expenseVO.getUserId());
		initialValues.put(KEY_GROUPID,expenseVO.getGroupId());
		initialValues.put(KEY_DESCRIPTION,expenseVO.getExpenseDescription());
		initialValues.put(KEY_RECEIVERID,expenseVO.getReceiverId());
		initialValues.put(KEY_CREATEID,expenseVO.getCreateId());
		initialValues.put(KEY_CREATEDATE,expenseVO.getCreateDate());
		initialValues.put(KEY_AMOUNT,expenseVO.getAmount());
		initialValues.put(KEY_STATUS,expenseVO.getStatus());
		
		getWritableDatabase().insert(DATABASE_TABLE_EXPENSE, null, initialValues);
		
	}
	
	public void insertUserExpense(ExpenseUserVO expenseUserVO) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USEREXPENSEID,expenseUserVO.getUserExpenseId());
		initialValues.put(KEY_EXPENSEID,expenseUserVO.getExpenseId());
		initialValues.put(KEY_USERID,expenseUserVO.getUserId());
		initialValues.put(KEY_CREATEDATE,expenseUserVO.getCreateDate());
		initialValues.put(KEY_AMOUNT,expenseUserVO.getAmount());
		
		getWritableDatabase().insert(DATABASE_TABLE_USER_EXPENSE, null, initialValues);
		
	}

	public Cursor getGroupWiseExpenseReport(int groupId) {
		String query = SQLUtil.GET_EXPENSE_REPORT_BY_GROUP;
		query = query.replace("@id", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}
	/*public Cursor dummy(int groupId) {
		String query = SQLUtil.DUMMY;
		query = query.replace("@id", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}*/

	public Cursor getUserListByGroup(int groupId) {
		String query = SQLUtil.GET_USER_LIST_BY_GROUP;
		query = query.replace("@id", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void deleteAllGroupInformation(int groupId) {
		
			String query = "";
			
			query = SQLUtil.DELETE_USER_EXPENSE_BY_GROUPID;
			query = query.replace("@id", String.valueOf(groupId));
			//System.out.println("query2 "+query);
			getWritableDatabase().execSQL(query);
		
			query = SQLUtil.DELETE_TABLE;
			query = query.replace("@id", String.valueOf(groupId));
			query = query.replace("@table", DATABASE_TABLE_GROUP);
			//System.out.println("query1 "+query);
			getWritableDatabase().execSQL(query);
			
			query = SQLUtil.DELETE_TABLE;
			query = query.replace("@id", String.valueOf(groupId));
			query = query.replace("@table", DATABASE_TABLE_GROUP_MEMBER);
			//System.out.println("query2 "+query);
			getWritableDatabase().execSQL(query);
			
			query = SQLUtil.DELETE_TABLE;
			query = query.replace("@id", String.valueOf(groupId));
			query = query.replace("@table", DATABASE_TABLE_EXPENSE);
			//System.out.println("query2 "+query);
			getWritableDatabase().execSQL(query);
			
			
			
			
	}

	public void deleteSessionUser() {
		String query = 	SQLUtil.DELETE_SESSION_TABLE;
		getWritableDatabase().execSQL(query);
	}

	public Cursor getGroupDetails(int groupId) {
		String query = SQLUtil.GET_GROUP_DETAILS;
		query = query.replace("@id", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public Cursor getAllUserList() {
		String query = SQLUtil.GET_ALL_USER_LIST;
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void changeUserStatus(String databaseTable, String status, int userId) {
		String query = SQLUtil.UPDATE_USER_STATUS;
		query = query.replace("@id", String.valueOf(userId))
				.replace("@status", status)
				.replace("@table", databaseTable);
		getWritableDatabase().execSQL(query);
		if(SplitShareDB.DATABASE_TABLE_USER.equalsIgnoreCase(databaseTable))
		{
			query = SQLUtil.UPDATE_USER_STATUS;
			query = query.replace("@id", String.valueOf(userId))
					.replace("@status", status)
					.replace("@table", DATABASE_TABLE_GROUP_MEMBER);
			getWritableDatabase().execSQL(query);
		}
				
				
		
	}

	public Cursor getUserDetailByUserId(int userId) {
		String query = SQLUtil.GET_USER_DETAIL;
		query = query.replace("@id", String.valueOf(userId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void deleteExpense(int expenseId) {
		String query = 	SQLUtil.DELETE_EXPENSE;
		query = query.replace("@id", String.valueOf(expenseId));
		getWritableDatabase().execSQL(query);
		
		deleteUserExpenseById(expenseId);
		
	}
	public void updateExpense(ExpenseVO expenseVO) {
		String query = 	SQLUtil.UPDATE_EXPENSE;
		query = query.replace("@id", String.valueOf(expenseVO.getExpenseId()))
				.replace("@amount", String.valueOf(expenseVO.getAmount()))
				.replace("@description", expenseVO.getExpenseDescription())
				.replace("@reciverId", String.valueOf(expenseVO.getReceiverId()))
				.replace("@status", expenseVO.getStatus());
		getWritableDatabase().execSQL(query);
	}
	
	public void deleteUserExpenseById(int expenseId){
		
		String query = 	SQLUtil.DELETE_USER_EXPENSE;
		query = query.replace("@id", String.valueOf(expenseId));
		getWritableDatabase().execSQL(query);
	}

	public Cursor getUserSession() {
		String query = SQLUtil.GET_SESSION;
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void insertSession(UserSessionVO userSessionVO) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID,userSessionVO.getSessionUserId());
		initialValues.put(KEY_CREATEDATE,userSessionVO.getSessionCreateDate());
		initialValues.put(KEY_STATUS,CommonConstant.STATUS_ACTIVE);
		
		getWritableDatabase().insert(DATABASE_TABLE_USER_SESSION, null, initialValues);
		
	}

	public Cursor getExpenseDetailByExpenseId(ExpenseVO expenseVO) {
		String query = SQLUtil.GET_EXPENSE_DETAIL_BY_EXPENSEID;
		query = query.replace("@id", String.valueOf(expenseVO.getExpenseId()));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void updateGroupDescrition(GroupVO groupVO) {
		String query = 	SQLUtil.UPDATE_GROUP_DESCRIPTION;
		query = query.replace("@id", String.valueOf(groupVO.getGroupId()))
				.replace("@description", groupVO.getGroupDescription())
				.replace("@cost", String.valueOf(groupVO.getEstimatedCost()));
		getWritableDatabase().execSQL(query);
		
	}
	
	public Cursor getExpenseByUserId(int userId)
	{
		String query = SQLUtil.GET_USER_COUNT_FROM_GROUP_BY_ID;
		query = query.replace("@id", String.valueOf(userId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public void deleteUser(int userId) {
		String query = 	SQLUtil.DELETE_USER;
		query = query.replace("@id", String.valueOf(userId))
				.replace("@table",DATABASE_TABLE_USER);
		getWritableDatabase().execSQL(query);
		query = query.replace("@id", String.valueOf(userId))
				.replace("@table",DATABASE_TABLE_GROUP_MEMBER);
		getWritableDatabase().execSQL(query);
		
	}

	public void updateUser(UserVO userVO) {
		String query = 	SQLUtil.UPDATE_USER;
		query = query.replace("@id", String.valueOf(userVO.getUserId()))
				.replace("@name", userVO.getName())
				.replace("@email", userVO.getEmail())
				.replace("@phone", userVO.getPhone());
		getWritableDatabase().execSQL(query);
	}

	public Cursor getIndividualExpenseDetails(int groupId) {
		String query = SQLUtil.GET_INDIVIDUAL_EXPENSE_GROUP_BY_ID;
		query = query.replace("@id", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}
	
	public Cursor getUserExpenseDetailsByGroupId(int groupId, int userId) {
		String query = SQLUtil.GET_USER_EXPENSE_BY_GROUP_ID;
		query = query.replace("@groupId", String.valueOf(groupId));
		query = query.replace("@userId", String.valueOf(userId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public Cursor getIndividualAdvanceTakenDetails(int groupId) {
		String query = SQLUtil.GET_INDIVIDUAL_ADVANCE_TAKEN_BY_GROUP_ID;
		query = query.replace("@id", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public Cursor calculateIndividualShare(int groupId) {
		String query = SQLUtil.CALCULATE_INDIVIDUAL_SHARE_FOR_GROUP;
		query = query.replace("@groupId", String.valueOf(groupId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public Cursor getUserExpenseListByExpenseId(int expenseId) {
		String query = SQLUtil.GET_ALL_USER_EXPENSE_BY_EXPENSE_ID;
		query = query.replace("@id", String.valueOf(expenseId));
		return (getReadableDatabase().rawQuery(query, null));
	}

	public Cursor testDBActivity() {
		
		
		String query = SQLUtil.DROP_TABLE_DUMMY;
		getWritableDatabase().execSQL(query);
		
		query = SQLUtil.CREATE_TABLE_DUMMY;
		getWritableDatabase().execSQL(query);
		
		query = SQLUtil.INSERT_TABLE_DUMMY;
		query = query.replace("@id", "10");
		getWritableDatabase().execSQL(query);
		
		query = SQLUtil.INSERT_TABLE_DUMMY;
		query = query.replace("@id", "20.30");
		getWritableDatabase().execSQL(query);
		
		query = SQLUtil.INSERT_TABLE_DUMMY;
		query = query.replace("@id", "30.27");
		getWritableDatabase().execSQL(query);
		
			
		query = SQLUtil.GET_TABLE_DUMMY;
		return (getReadableDatabase().rawQuery(query, null));
	}

}
