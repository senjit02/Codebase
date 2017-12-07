package com.ishare.split.util;

public class SQLUtil {

	//DDL Create
	public static final String TABLE_CREATE_GROUP = "CREATE TABLE IF NOT EXISTS USERGROUP(GROUPID INTEGER,GROUPNAME VARCHAR,DESCRIPTION VARCHAR,CREATEDATE DATE,CREATEID INTEGER,STATUS VARCHAR,ESTIMATEDCOST INTEGER);";
	public static final String TABLE_CREATE_GROUP_SEQUENCE = "CREATE TABLE IF NOT EXISTS USERGROUP_SEQUENCE(SEQUENCE INTEGER,CREATEDATE DATE);";
	public static final String TABLE_CREATE_USER = "CREATE TABLE IF NOT EXISTS USER(USERID INTEGER,NAME VARCHAR,USERCODE VARCHAR,ROLEID INTEGER,EMAIL VARCHAR,PHONE VARCHAR,EMERGENCYNAME VARCHAR,EMERGENCYNUMBER VARCHAR,CREATEDATE DATE,STATUS VARCHAR)";
	public static final String TABLE_CREATE_USER_SEQUENCE = "CREATE TABLE IF NOT EXISTS USER_SEQUENCE(SEQUENCE INTEGER,CREATEDATE DATE);";
	public static final String TABLE_CREATE_USER_SESSION = "CREATE TABLE IF NOT EXISTS USER_SESSION(USERID INTEGER,CREATEDATE DATE,STATUS VARCHAR)";
	public static final String TABLE_CREATE_GROUP_MEMBER = "CREATE TABLE IF NOT EXISTS GROUP_MEMBER(GROUPID INTEGER,USERID INTEGER,GROUPROLEID INTEGER,CREATEDATE DATE,CREATEID INTEGER,STATUS VARCHAR);";
	public static final String TABLE_CREATE_EXPENSE = "CREATE TABLE IF NOT EXISTS EXPENSE(EXPENSEID INTEGER,GROUPID INTEGER,USERID INTEGER,AMOUNT INTEGER,DESCRIPTION VARCHAR,RECEIVERID INTEGER,CREATEDATE DATE,CREATEID INTEGER,STATUS VARCHAR);";
	public static final String TABLE_CREATE_EXPENSE_SEQUENCE = "CREATE TABLE IF NOT EXISTS EXPENSE_SEQUENCE(SEQUENCE INTEGER,CREATEDATE DATE);";
	public static final String TABLE_CREATE_USER_EXPENSE = "CREATE TABLE IF NOT EXISTS USER_EXPENSE(USEREXPENSEID INTEGER,EXPENSEID INTEGER,USERID INTEGER,AMOUNT INTEGER,CREATEDATE DATE);";
	public static final String TABLE_CREATE_USER_EXPENSE_SEQUENCE = "CREATE TABLE IF NOT EXISTS USER_EXPENSE_SEQUENCE(SEQUENCE INTEGER,CREATEDATE DATE);";
	
	
	
	//DML select
	public static final String GET_SESSION = "SELECT USERID,CREATEDATE FROM USER_SESSION";
	public static final String GET_MAX_GROUP_SEQUENCE = "SELECT MAX(SEQUENCE) FROM USERGROUP_SEQUENCE";
	public static final String GET_MAX_USER_SEQUENCE = "SELECT MAX(SEQUENCE) FROM USER_SEQUENCE";
	public static final String GET_MAX_EXEPENSE_SEQUENCE = "SELECT MAX(SEQUENCE) FROM EXPENSE_SEQUENCE";
	public static final String GET_MAX_USER_EXEPENSE_SEQUENCE = "SELECT MAX(SEQUENCE) FROM USER_EXPENSE_SEQUENCE";
	public static final String GET_GROUP_LIST = "SELECT GROUPID,GROUPNAME,DESCRIPTION,CREATEDATE,CREATEID,STATUS FROM USERGROUP WHERE STATUS = '@status'";
	public static final String GET_USER_LIST_TO_ADD = " SELECT " +
			" A.USERID,A.NAME,A.STATUS " +
			" FROM USER A " +
			" WHERE A.STATUS = 'A' AND A.USERID NOT IN ( SELECT B.USERID" +
			" FROM GROUP_MEMBER B" +
			" WHERE B.GROUPID = @id )" +
			" ORDER BY A.NAME ";
	public static final String GET_EXPENSE_REPORT_BY_GROUP = " SELECT " +
			" A.EXPENSEID,A.GROUPID, " +
			" B.NAME,A.USERID,A.AMOUNT, " +
			" A.DESCRIPTION,A.RECEIVERID,A.CREATEDATE, " +
			" A.CREATEID,A.STATUS " +
			" FROM EXPENSE A " +
			" INNER JOIN USER B " + 
			" ON A.USERID = B.USERID " +
			" WHERE A.GROUPID = @id";
	public static final String GET_EXPENSE_REPORT_BY_GROUP1 = " SELECT " +
			" A.EXPENSEID,A.GROUPID, " +
			" B.NAME,A.USERID,A.AMOUNT, " +
			" A.DESCRIPTION,A.RECEIVERID,A.CREATEDATE, " +
			" A.CREATEID,A.STATUS " +
			" FROM " +
			" GROUP_MEMBER G " +
			" INNER JOIN " +
			" USER B " +
			" ON G.USERID = B.USERID " +
			" AND G.GROUPID = @id " +
			" LEFT OUTER JOIN " +
			" EXPENSE A " +
			" ON B.USERID = A.USERID " +
			" WHERE A.GROUPID = @id";
	
	public static final String GET_USER_LIST_BY_GROUP = " SELECT " +
			" A.USERID, A.NAME,A.ROLEID,A.EMAIL,A.PHONE,B.GROUPROLEID,A.STATUS " +
			" FROM USER A " +
			" INNER JOIN GROUP_MEMBER B " +
			" ON A.USERID = B.USERID " +
			" WHERE B.GROUPID = @id" +
			" ORDER BY A.NAME ";
	public static final String GET_ALL_USER_LIST = "SELECT USERID,NAME,USERCODE,ROLEID,EMAIL,PHONE,EMERGENCYNAME,EMERGENCYNUMBER,CREATEDATE,STATUS FROM USER ORDER BY NAME";
	public static final String GET_USER_DETAIL = "SELECT USERID,NAME,USERCODE,ROLEID,EMAIL,PHONE,EMERGENCYNAME,EMERGENCYNUMBER,CREATEDATE,STATUS FROM USER WHERE USERID = @id";
	public static final String GET_GROUP_DETAILS = " SELECT " +
			" A.GROUPID,A.GROUPNAME, " +
			" A.DESCRIPTION,A.CREATEDATE,B.NAME,A.ESTIMATEDCOST " +
			" FROM USERGROUP A " +
			" LEFT OUTER JOIN USER B " +
			" ON A.CREATEID = B.USERID " +
			" WHERE A.GROUPID = @id";
	public static final String GET_EXPENSE_DETAIL_BY_EXPENSEID = " SELECT " +
			" EXPENSEID,GROUPID,USERID,AMOUNT,DESCRIPTION,RECEIVERID,CREATEDATE,CREATEID,STATUS" +
			" FROM EXPENSE " +
			" WHERE EXPENSEID = @id";
	public static final String GET_USER_COUNT_FROM_GROUP_BY_ID = "SELECT COUNT(USERID) FROM GROUP_MEMBER " +
			" WHERE USERID = @id";
	public static final String GET_INDIVIDUAL_EXPENSE_GROUP_BY_ID = "SELECT " +
			" A.USERID,A.NAME, " +
			" SUM(B.AMOUNT)" +
			" ,B.STATUS " +
			" FROM " +
			" USER A " +
			" INNER JOIN EXPENSE B " +
			" ON A.USERID = B.USERID " +
			" WHERE B.GROUPID = @id" +
			" GROUP BY A.USERID,A.NAME" +
			" ,B.STATUS " +
			" ORDER BY A.NAME";
	
	 public static final String GET_INDIVIDUAL_ADVANCE_TAKEN_BY_GROUP_ID = "SELECT " +
             " A.USERID,A.NAME, " +
             " SUM(B.AMOUNT) " +
             " ,B.STATUS " +
             " FROM " +
             " USER A " +
             " INNER JOIN EXPENSE B " +
             " ON A.USERID = B.RECEIVERID " +
             " WHERE B.GROUPID = @id"+
             " AND B.STATUS = 'A'"+
             " GROUP BY A.USERID,A.NAME " +
             " ,B.STATUS " +
             " ORDER BY A.NAME";
	 
	 public static final String GET_USER_EXPENSE_BY_GROUP_ID = "SELECT " +
             " A.USERID,A.NAME, " +
             " B.AMOUNT,B.DESCRIPTION,'E' AS STATUS,B.CREATEDATE, " +
             " 'dummy' AS RECEIVER " +
             " FROM " +
             " USER A " +
             " INNER JOIN EXPENSE B " +
             " ON A.USERID = B.USERID " +
             " WHERE B.GROUPID = @groupId "+
             " AND A.USERID = @userId " +
             " AND B.STATUS = 'E' " +
             " UNION "+
             " SELECT " +
             " A.USERID,A.NAME, " +         
             " B.AMOUNT,B.DESCRIPTION,'A' AS STATUS,B.CREATEDATE, " +
             " C.NAME AS RECEIVER " +
             " FROM " +
             " USER A " +
             " INNER JOIN EXPENSE B " +
             " ON A.USERID = B.USERID " +
             " INNER JOIN USER C " +
             " ON B.RECEIVERID = C.USERID " +
             " WHERE B.GROUPID = @groupId "+
             " AND A.USERID = @userId " +
             " AND B.STATUS = 'A' " +
             " UNION "+
             " SELECT " +
             " A.USERID,A.NAME, " +
             " B.AMOUNT,B.DESCRIPTION,'R' AS STATUS,B.CREATEDATE, " +
             " C.NAME AS RECEIVER " +
             " FROM " +
             " USER A " +
             " INNER JOIN EXPENSE B " +
             " ON A.USERID = B.RECEIVERID " +
             " INNER JOIN USER C " +
             " ON C.USERID = B.USERID " +
             " WHERE B.GROUPID = @groupId "+
             " AND A.USERID = @userId " +
             " AND B.STATUS = 'A' " +
             " ORDER BY B.CREATEDATE " ;
	 
	 public static final String CALCULATE_INDIVIDUAL_SHARE_FOR_GROUP = "SELECT " +
			 " B.USERID, SUM(B.AMOUNT) AS AMOUNT " +
			 " FROM " +
			 " EXPENSE A " +
			 " INNER JOIN " +
			 " USER_EXPENSE B " +
			 " ON A.EXPENSEID = B.EXPENSEID " +
			 " WHERE A.GROUPID = @groupId " +
			 " GROUP BY B.USERID ";
	 
	 public static final String GET_ALL_USER_EXPENSE_BY_EXPENSE_ID = "SELECT " +
	 		" USEREXPENSEID,EXPENSEID,USERID,AMOUNT,CREATEDATE " +
	 		" FROM " +
	 		" USER_EXPENSE " +
	 		" WHERE EXPENSEID = @id";
	
	//UPDATE
	public static final String UPDATE_USER_STATUS = " UPDATE @table SET STATUS = '@status' WHERE USERID = @id";
	public static final String UPDATE_EXPENSE = "UPDATE EXPENSE " +
			" SET AMOUNT = @amount," +
			" DESCRIPTION = '@description'," +
			" RECEIVERID =  @reciverId, " +
			" STATUS = '@status' " +
			" WHERE EXPENSEID = @id";
	public static final String UPDATE_GROUP_DESCRIPTION = "UPDATE USERGROUP SET DESCRIPTION= '@description',ESTIMATEDCOST=@cost WHERE GROUPID = @id";
	public static final String UPDATE_USER = "UPDATE USER SET NAME='@name',EMAIL='@email',PHONE='@phone' WHERE USERID = @id";
	
	//DELETE
	public static final String DELETE_TABLE = "DELETE FROM @table WHERE GROUPID = @id";
	public static final String DELETE_SESSION_TABLE = "DELETE FROM USER_SESSION";
	public static final String DELETE_EXPENSE = "DELETE FROM EXPENSE WHERE EXPENSEID = @id";
	public static final String DELETE_USER = "DELETE FROM @table WHERE USERID = @id";
	public static final String DELETE_USER_EXPENSE = "DELETE FROM USER_EXPENSE WHERE EXPENSEID = @id";
	public static final String DELETE_USER_EXPENSE_BY_GROUPID = "DELETE FROM USER_EXPENSE " +
			" WHERE EXPENSEID = (" +
			" SELECT EXPENSEID FROM EXPENSE WHERE GROUPID = @id )";
	
	//DUMMY Activity
	public static final String CREATE_TABLE_DUMMY = "CREATE TABLE IF NOT EXISTS DUMMY(ID REAL);";
	public static final String INSERT_TABLE_DUMMY = "INSERT INTO DUMMY VALUES(@id)";
	public static final String GET_TABLE_DUMMY = "SELECT * FROM DUMMY ";
	public static final String DROP_TABLE_DUMMY = "DROP TABLE DUMMY ;";
	
	
	
	
	
	
	
	
	
	
	

	
	
	

}