package com.ishare.split.activity;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.vo.UserSessionVO;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewMemberExpense extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	ImageView homeImage;
	TextView headerMsg;
	TextView expenseSummaryHeader;
	TextView expenseSummary;
	TextView expenseDetail;
	TextView advanceSummaryHeader;
	TextView advanceSummary;
	TextView advanceDetail;
	TextView advanceTakenSummaryHeader;
	TextView advanceTakenSummary;
	TextView advanceTakenDetail;
	Cursor cursor;
	int groupId;
	int userId;
	String userName;
	
	int sumExpense=0;
	String expenseSummaryText="";
	String expenseDetailsText="";
	
	int sumAdvanceGiven=0;
	String advanceSummaryText="";
	String advanceDetailText="";
	
	int sumAdvanceTaken=0;
	String advanceTakenSummaryText="";
	String advanceTakenDetailText="";
	
	int expenseCounter=0;
	int advanceGivenCounter=0;
	int advanceTakenCounter=0;

	boolean expenseSummaryHeaderShowFlag = true;
	boolean advanceSummaryHeaderShowFlag = false;
	boolean advanceTakenSummaryHeaderShowFlag = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_member_expense);

		savedInstanceState = getIntent().getExtras();
		final Context context  = this;

		init(savedInstanceState, context);

		initializeViewElements();


		performCommonTask(context);


		expenseSummaryHeaderViewManager();

		//----- Get the expense details for the user ---//
		getExpenseDetailForUser();
		
		splitShareDB.closeDBConnection(cursor);
	}

	@SuppressLint("NewApi")
	private void getExpenseDetailForUser() {
		cursor = splitShareDB.getUserExpenseDetailsByGroupId(groupId,userId);
		if(null!=cursor && cursor.moveToFirst())
		{
			do{
				if(CommonConstant.EXPENSE_STATUS_EXPENSE.equalsIgnoreCase(cursor.getString(4))){
					sumExpense = sumExpense + cursor.getInt(2);
					expenseCounter = expenseCounter+1;
					expenseDetailsText = expenseDetailsText 
							+ String.valueOf(expenseCounter) + ". " 
							+ "Details: "
							+ cursor.getString(3) + "\n"
							+ "Amount Rs. " 
							+ cursor.getString(2) + "/-"+ "\n"
							+ "Date: " 
							+ cursor.getString(5) + "\n\n";
					
				}
				else if(CommonConstant.EXPENSE_STATUS_ADVANCE.equalsIgnoreCase(cursor.getString(4))){
					sumAdvanceGiven = sumAdvanceGiven + cursor.getInt(2);
					advanceGivenCounter = advanceGivenCounter+1;
					advanceDetailText = advanceDetailText 
							+ String.valueOf(advanceGivenCounter) + ". " 
							+ "Details: "
							+ cursor.getString(3) + "\n"
							+ "Amount Rs. " 
							+ cursor.getString(2) + "/-"+ "\n"
							+ "Given to: " 
							+ cursor.getString(6) + "\n"
							+ "Date: " 
							+ cursor.getString(5) + "\n\n";
				}
				else if(CommonConstant.EXPENSE_STATUS_ADVANCE_RECEIVE.equalsIgnoreCase(cursor.getString(4))){
					sumAdvanceTaken = sumAdvanceTaken + cursor.getInt(2);
					advanceTakenCounter = advanceTakenCounter+1;
					advanceTakenDetailText = advanceTakenDetailText 
							+ String.valueOf(advanceTakenCounter) + ". " 
							+ "Details: "
							+ cursor.getString(3) + "\n"
							+ "Amount Rs, " 
							+ cursor.getString(2) + "/-"+ "\n"
							+ "Taken from: " 
							+ cursor.getString(6) + "\n"
							+ "Date: " 
							+ cursor.getString(5) + "\n\n";
					}
				
				
			}while(cursor.moveToNext());
		}
		
		if(expenseDetailsText.trim().isEmpty())
			expenseDetailsText = CommonConstant.NO_DATA;
		if(advanceDetailText.trim().isEmpty())
			advanceDetailText = CommonConstant.NO_DATA;
		if(advanceTakenDetailText.trim().isEmpty())
			advanceTakenDetailText = CommonConstant.NO_DATA;
		
		expenseSummary.setText("Total expense Rs "+String.valueOf(sumExpense)+"/-");
		//expenseSummary.setText(Html.fromHtml("<span style='border-bottom:1px solid'>" + sumExpense + "</span>"));
		expenseDetail.setText(expenseDetailsText);
		
		advanceSummary.setText("Total advance given Rs "+String.valueOf(sumAdvanceGiven)+"/-");
		advanceDetail.setText(advanceDetailText);
		
		advanceTakenSummary.setText("Total advance taken Rs "+String.valueOf(sumAdvanceTaken)+"/-");
		advanceTakenDetail.setText(advanceTakenDetailText);
	}

	private void performCommonTask(final Context context) {
		headerMsg.setText("   Expense Details for "+userName);

		homeImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommonConstant.ADMIN_ACTIVITY);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				startActivity(intent);	

			}
		});
	}

	private void init(Bundle savedInstanceState, final Context context) {
		splitShareDB = new SplitShareDB(context);

		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		groupId = savedInstanceState.getInt(CommonConstant.TRANSFER_GROUP_ID);
		userId = savedInstanceState.getInt(CommonConstant.TRANSFER_USER_ID);
		userName = savedInstanceState.getString(CommonConstant.TRANSFER_USER_NAME);
	}

	private void initializeViewElements() {
		expenseSummaryHeader = (TextView)findViewById(R.id.expenseSummaryHeader);
		expenseSummary = (TextView)findViewById(R.id.expenseSummary);
		expenseDetail = (TextView)findViewById(R.id.expenseDetail);
		advanceSummaryHeader = (TextView)findViewById(R.id.advanceSummaryHeader);
		advanceSummary = (TextView)findViewById(R.id.advanceSummary);
		advanceDetail = (TextView)findViewById(R.id.advanceDetail);
		advanceTakenSummaryHeader = (TextView)findViewById(R.id.advanceTakenSummaryHeader);
		advanceTakenSummary = (TextView)findViewById(R.id.advanceTakenSummary);
		advanceTakenDetail = (TextView)findViewById(R.id.advanceTakenDetail);
		headerMsg = (TextView)findViewById(R.id.headerMsg);
		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		homeImage = (ImageView)iconLayout.findViewById(R.id.homeImage);

		advanceDetail.setVisibility(View.GONE);
		advanceTakenDetail.setVisibility(View.GONE);

	}

	private void expenseSummaryHeaderViewManager() {
		//---- expenseSummaryHeader onclick listener ----//
		expenseSummaryHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!expenseSummaryHeaderShowFlag)
				{
					expenseSummaryHeader.setText(" - Expense Detail ");
					expenseDetail.setVisibility(View.VISIBLE);
					expenseSummaryHeaderShowFlag = true;
				}
				else if(expenseSummaryHeaderShowFlag)
				{

					expenseSummaryHeader.setText(" + Expense Detail ");
					expenseDetail.setVisibility(View.GONE);
					expenseSummaryHeaderShowFlag = false;
				}

			}
		});
		//---------------------------------------------//

		//---- Advance onclick listener ----//
		advanceSummaryHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!advanceSummaryHeaderShowFlag)
				{
					advanceSummaryHeader.setText(" - Advance Given Summary ");
					advanceDetail.setVisibility(View.VISIBLE);
					advanceSummaryHeaderShowFlag = true;
				}
				else if(advanceSummaryHeaderShowFlag)
				{

					advanceSummaryHeader.setText(" + Advance Given Summary ");
					advanceDetail.setVisibility(View.GONE);
					advanceSummaryHeaderShowFlag = false;
				}

			}
		});
		advanceTakenSummaryHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!advanceTakenSummaryHeaderShowFlag)
				{
					advanceTakenSummaryHeader.setText(" - Advance Taken Summary ");
					advanceTakenDetail.setVisibility(View.VISIBLE);
					advanceTakenSummaryHeaderShowFlag = true;
				}
				else if(advanceTakenSummaryHeaderShowFlag)
				{

					advanceTakenSummaryHeader.setText(" + Advance Taken Summary ");
					advanceTakenDetail.setVisibility(View.GONE);
					advanceTakenSummaryHeaderShowFlag = false;
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.view_member_expense, menu);
		return true;
	}

}
