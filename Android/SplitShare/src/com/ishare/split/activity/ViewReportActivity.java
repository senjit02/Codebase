package com.ishare.split.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ishare.split.constant.CommonConstant;
import com.ishare.split.constant.MessageConstant;
import com.ishare.split.db.SplitShareDB;
import com.ishare.split.util.DateUtil;
import com.ishare.split.vo.ExpenseUserVO;
import com.ishare.split.vo.ExpenseVO;
import com.ishare.split.vo.EstimateVO;
import com.ishare.split.vo.MailVO;
import com.ishare.split.vo.UserGroupVO;
import com.ishare.split.vo.UserSessionVO;
import com.ishare.split.vo.UserVO;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

public class ViewReportActivity extends BaseActivity {

	SplitShareDB splitShareDB;
	UserSessionVO userSessionVO;
	Button addMember;
	View mailContentView;
	ImageView crossImage;
	ImageView sendMail;
	TextView reportDetail;
	TextView reportSummary;
	TextView reportSummaryCost;
	TextView balanceSummary;
	UserGroupVO userGroupVO;
	TextView perHeadContributionHeader;
	TextView balanceSummaryHeader;
	TextView reportDetailHeader;
	TextView individualSummaryHeader;
	TextView individualSummaryDetail;
	LinearLayout listLinearLayOut;
	Cursor cursor;
	int groupId;
	String selectedGroupName;
	int totalAmount = 0; 
	String reportDetailText = "";
	String reportSummaryText = "";
	String reportSummaryCostText = "";
	String balanceSummaryText = ""; 
	String individualSummaryText = "";
	ImageView homeImage;
	int perHeadCost = 0;
	int estimatedeCost = 0;
	String groupName;
	String mailBody;
	TextView headerMsg;
	boolean perheadContributionShowFlag = false;
	boolean balanceSummaryShowFlag = false;
	boolean reportDetailShowFlag = false;
	boolean individualSummaryShowFlag = false;
	Map<Integer,UserVO> groupMemberMap;
	Map<Integer,String> mailContentMap = new TreeMap<Integer, String>();
	int sumExpense=0;
	int sumAdvanceGiven=0;
	int sumAdvanceTaken=0;

	int expenseCounter=0;
	int advanceGivenCounter=0;
	int advanceTakenCounter=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_report);
		savedInstanceState = getIntent().getExtras();
		final Context context  = this;
		splitShareDB = new SplitShareDB(context);
		userSessionVO = (UserSessionVO) savedInstanceState.getSerializable(CommonConstant.USER_SESSIONVO);
		groupId = savedInstanceState.getInt(CommonConstant.TRANSFER_GROUP_ID);

		initializeViewElement(savedInstanceState);

		performCommonTask(context);

		reportViewManager();

		sendMailListener(context);

		groupMemberMap = getGroupMember(groupId);

		// --- Get all expense for the group -----//
		Map<Integer, ExpenseUserVO> estimateUserExpenseMap = populateAllExpenseDetailForGroup();
		if(null!=estimateUserExpenseMap && !estimateUserExpenseMap.isEmpty())
			prepareExpenseSummaryReport(estimateUserExpenseMap);

		//--- Get individual expense details ---------//
		getIndividualExpenseDetails(groupId);

		//-- set the values-------------------//
		reportSummary.setText(reportSummaryText);
		balanceSummary.setText(balanceSummaryText);
		individualSummaryDetail.setText(individualSummaryText);
		reportDetail.setText(reportDetailText);
		reportSummaryCost.setText(reportSummaryCostText);

		splitShareDB.closeDBConnection(cursor);
	}

	private void prepareExpenseSummaryReport(Map<Integer, ExpenseUserVO> estimateUserExpenseMap) {

		cursor = splitShareDB.getGroupDetails(groupId);

		if(null!=cursor && cursor.moveToFirst())
		{
			estimatedeCost = cursor.getInt(5);
			groupName =  cursor.getString(1);
		}
		reportSummaryCostText = reportSummaryCostText +  "* Estimated expense was Rs. "+estimatedeCost+"/-\n";
		reportSummaryCost.setText(reportSummaryCostText);

		cursor = null;
		cursor = splitShareDB.calculateIndividualShare(groupId);
		int counter = 1;
		int userId = 0;
		int balance = 0;
		String memberPerheadCost = "";
		String memberBalanceReport = "";
		if(null!=cursor && cursor.moveToFirst())
		{
			do{
				userId = cursor.getInt(0);
				System.out.println("userid in prepareExpenseSummaryReport>> "+userId);

				if(estimateUserExpenseMap.containsKey(userId))
				{
					ExpenseUserVO expenseUserVO = estimateUserExpenseMap.get(userId);
					expenseUserVO.setPerHeadCost(cursor.getInt(1));
					System.out.println("perhead calculated: "+cursor.getInt(1));
					balance = expenseUserVO.getPerHeadCost() - (expenseUserVO.getTotalExpenseDone() + expenseUserVO.getAdvanceGiven() - expenseUserVO.getAdvanceTaken());
					expenseUserVO.setBalance(balance);
				}
			}while(cursor.moveToNext());
		}	


		Iterator<Integer> iterator = estimateUserExpenseMap.keySet().iterator();
		while(iterator.hasNext()){
			ExpenseUserVO expenseUserVO = estimateUserExpenseMap.get(iterator.next()); 

			memberPerheadCost = memberPerheadCost +
					"* " +
					expenseUserVO.getUserName() + ": " +
					"Rs. " + expenseUserVO.getPerHeadCost() + "/-\n\n";

			if(expenseUserVO.getBalance() < 0){

				memberBalanceReport = memberBalanceReport +
						counter + ") " +
						expenseUserVO.getUserName() + CommonConstant.RECEIVE_MONEY + " Rs. " + Math.abs(expenseUserVO.getBalance())+ "/-\n\n";

			}else if(expenseUserVO.getBalance() > 0){

				memberBalanceReport = memberBalanceReport +
						counter + ") " +
						expenseUserVO.getUserName() + CommonConstant.DEPOSIT_MONEY + " Rs. " + Math.abs(expenseUserVO.getBalance())+ "/-\n\n";
			}else if(expenseUserVO.getBalance() == 0){

				memberBalanceReport = memberBalanceReport +
						counter + ") " +
						expenseUserVO.getUserName() + ": "+CommonConstant.NO_DUE+ "\n\n";
			}


			counter++;
		}


		reportSummaryText= memberPerheadCost;
		balanceSummaryText =  memberBalanceReport;

	}

	private void reportViewManager() {

		//---- Per Head Contribution Header onclick listener ----//
		perHeadContributionHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!perheadContributionShowFlag)
				{
					perHeadContributionHeader.setText(" - Member's Contribution Per Head  ");
					reportSummary.setVisibility(View.VISIBLE);
					perheadContributionShowFlag = true;
				}
				else if(perheadContributionShowFlag)
				{

					perHeadContributionHeader.setText(" + Member's Contribution Per Head  ");
					reportSummary.setVisibility(View.GONE);
					perheadContributionShowFlag = false;
				}

			}
		});

		//---- Balance Header onclick listener ----//
		balanceSummaryHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!balanceSummaryShowFlag)
				{
					balanceSummaryHeader.setText(" - Balance Summary  ");
					balanceSummary.setVisibility(View.VISIBLE);
					balanceSummaryShowFlag = true;
				}
				else if(balanceSummaryShowFlag)
				{

					balanceSummaryHeader.setText(" + Balance Summary  ");
					balanceSummary.setVisibility(View.GONE);
					balanceSummaryShowFlag = false;
				}

			}
		});

		//---- individualSummaryHeader onclick listener ----//
		individualSummaryHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!individualSummaryShowFlag)
				{
					individualSummaryHeader.setText(" - Individual Expense Summary ");
					individualSummaryDetail.setVisibility(View.VISIBLE);
					individualSummaryShowFlag = true;
				}
				else if(individualSummaryShowFlag)
				{

					individualSummaryHeader.setText(" + Individual Expense Summary ");
					individualSummaryDetail.setVisibility(View.GONE);
					individualSummaryShowFlag = false;
				}

			}
		});

		//---- reportDetailHeader onclick listener ----//
		reportDetailHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!reportDetailShowFlag)
				{
					reportDetailHeader.setText(" - Expense Details  ");
					reportDetail.setVisibility(View.VISIBLE);
					reportDetailShowFlag = true;
				}
				else if(reportDetailShowFlag)
				{

					reportDetailHeader.setText(" + Expense Details  ");
					reportDetail.setVisibility(View.GONE);
					reportDetailShowFlag = false;
				}

			}
		});
	}

	private void sendMailListener(final Context context) {
		sendMail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);  
				mailContentView = layoutInflater.inflate(R.layout.mailcontent_checklist_layout, null);
				final PopupWindow popupWindow = new PopupWindow(mailContentView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				popupWindow.setTouchable(true);
				popupWindow.setFocusable(true);  
				popupWindow.showAtLocation(mailContentView, Gravity.CENTER, 2,10);

				listLinearLayOut = (LinearLayout)mailContentView.findViewById(R.id.listLinearLayOut);
				RelativeLayout rl = (RelativeLayout)mailContentView.findViewById(R.id.crossImageLayout);
				crossImage = (ImageView)rl.findViewById(R.id.crossImage);
				Button submitPres = (Button)mailContentView.findViewById(R.id.submitPres);
				CheckBox reportSummaryChk = (CheckBox)listLinearLayOut.findViewById(R.id.reportSummaryChk);
				CheckBox balanceSummaryChk = (CheckBox)listLinearLayOut.findViewById(R.id.balanceSummaryChk);
				CheckBox individualSummaryChk = (CheckBox)listLinearLayOut.findViewById(R.id.individualSummaryChk);
				CheckBox expenseDetailChk = (CheckBox)listLinearLayOut.findViewById(R.id.expenseDetailChk);


				reportSummaryChk.setOnClickListener(getOnClickMailContent(CommonConstant.SELECTION_REPORT_SUMMARY,reportSummaryChk));
				balanceSummaryChk.setOnClickListener(getOnClickMailContent(CommonConstant.SELECTION_BALANCE_REPORT_SUMMARY,balanceSummaryChk));
				individualSummaryChk.setOnClickListener(getOnClickMailContent(CommonConstant.SELECTION_INDIVIDUAL_REPORT_SUMMARY,individualSummaryChk));
				expenseDetailChk.setOnClickListener(getOnClickMailContent(CommonConstant.SELECTION_EXPENSE_DETAIL_REPORT,expenseDetailChk));

				crossImage.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
					}});


				submitPres.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if(null==mailContentMap || mailContentMap.isEmpty()){
							Toast toast = Toast.makeText(ViewReportActivity.this, "Make atlest one selection",Toast.LENGTH_LONG);
							toast.show();
						}else{
							popupWindow.dismiss();
							System.out.println("finally mailContentMap "+mailContentMap);
							startMailSendActivity(context);
						}
							
						

						//System.out.println("finally mailContentMap "+mailContentMap);

					}
				});



			}

			private void startMailSendActivity(final Context context) {

				mailBody = "Hello,\n\nExpense Details for "+ groupName +" are listed below\n\n";
				mailBody = mailBody +
						"\n\nTotal expense: " 
						+"Rs. "+totalAmount+"/-\n\n"+"\n";

				Iterator<Integer> iterator = mailContentMap.keySet().iterator();
				while(iterator.hasNext()){
					int selection = iterator.next();
					if(CommonConstant.SELECTION_REPORT_SUMMARY == selection){
						mailBody = mailBody 
						    + "\n\n" + MessageConstant.LABEL_BORDER +"\n";
						mailBody = mailBody 
								+ "   Member's Perhead Contribution Report "+"\n";
						mailBody = mailBody 
								+ MessageConstant.LABEL_BORDER + "\n\n";
					}
					else if(CommonConstant.SELECTION_BALANCE_REPORT_SUMMARY == selection){
						mailBody = mailBody 
							+ "\n\n" + MessageConstant.LABEL_BORDER+"\n";
						mailBody = mailBody 
								+ "   Balance Report Summary"+"\n";
						mailBody = mailBody 
								+ MessageConstant.LABEL_BORDER +"\n\n";
					}
					else if(CommonConstant.SELECTION_INDIVIDUAL_REPORT_SUMMARY == selection){
						mailBody = mailBody 
								+ "\n\n" + MessageConstant.LABEL_BORDER+"\n";
						mailBody = mailBody 
								+ "   Individual Total Expense Report"+"\n";
						mailBody = mailBody 
								+ MessageConstant.LABEL_BORDER + "\n\n";
					}
					else if(CommonConstant.SELECTION_EXPENSE_DETAIL_REPORT == selection){
						mailBody = mailBody 
								+ "\n\n" + MessageConstant.LABEL_BORDER+"\n";
						mailBody = mailBody 
								+ "   Detailed Expense Report "+"\n";
						mailBody = mailBody 
								+ MessageConstant.LABEL_BORDER + "\n\n";
					}
					mailBody = mailBody + 
							mailContentMap.get(selection);
				}
				MailVO mailVO =new MailVO();
				mailVO.setMailSubject("Split_N_Share | Group: "+groupName+" | Expense History as of "+DateUtil.getDateFormat(
						Calendar.getInstance().getTime(), CommonConstant.DATE_FORMAT));
				mailVO.setMailBody(mailBody);

				 
				System.out.println("mailBody " +mailVO.getMailBody());
				Intent intent = new Intent(context, MailActivity.class);
				intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
				intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, groupId);
				intent.putExtra(CommonConstant.MAIL_VO, mailVO);
				startActivity(intent);
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

	private void initializeViewElement(Bundle savedInstanceState) {
		selectedGroupName = savedInstanceState.getString(CommonConstant.TRANSFER_GROUP_NAME);
		reportDetail = (TextView)findViewById(R.id.reportDetail);
		reportSummary = (TextView)findViewById(R.id.reportSummary);
		reportSummaryCost = (TextView)findViewById(R.id.reportSummaryCost);
		balanceSummary = (TextView)findViewById(R.id.balanceSummary);
		perHeadContributionHeader = (TextView)findViewById(R.id.perHeadContributionHeader);
		balanceSummaryHeader = (TextView)findViewById(R.id.balanceSummaryHeader);
		reportDetailHeader = (TextView)findViewById(R.id.reportDetailHeader);
		individualSummaryHeader = (TextView)findViewById(R.id.individualSummaryHeader);
		individualSummaryDetail = (TextView)findViewById(R.id.individualSummaryDetail);
		headerMsg = (TextView)findViewById(R.id.headerMsg);
		headerMsg.setText("   Expense Details for "+selectedGroupName);
		sendMail = (ImageView)findViewById(R.id.sendMail);

		RelativeLayout iconLayout = (RelativeLayout)findViewById(R.id.iconLayout);
		homeImage= (ImageView)iconLayout.findViewById(R.id.homeImage);
	}

	@SuppressLint("NewApi")
	private void getIndividualExpenseDetails(int groupId){

		Iterator<Integer> iterator = groupMemberMap.keySet().iterator();
		int userId = 0;
		int counter = 1;
		String userName = "";
		while(iterator.hasNext())
		{
			userId = iterator.next();
			userName = groupMemberMap.get(userId).getName();
			sumExpense = 0;
			sumAdvanceGiven = 0;
			sumAdvanceTaken = 0;
			cursor = splitShareDB.getUserExpenseDetailsByGroupId(groupId,userId);
			if(null!=cursor && cursor.moveToFirst())
			{
				do{
					if(CommonConstant.EXPENSE_STATUS_EXPENSE.equalsIgnoreCase(cursor.getString(4))){
						sumExpense = sumExpense + cursor.getInt(2);

					}else if(CommonConstant.EXPENSE_STATUS_ADVANCE.equalsIgnoreCase(cursor.getString(4))){
						sumAdvanceGiven = sumAdvanceGiven + cursor.getInt(2);

					}else if(CommonConstant.EXPENSE_STATUS_ADVANCE_RECEIVE.equalsIgnoreCase(cursor.getString(4))){
						sumAdvanceTaken = sumAdvanceTaken + cursor.getInt(2);

					}



				}while(cursor.moveToNext());
			}
			individualSummaryText = individualSummaryText
					+ counter + ") "
					+ userName +" - "
					+ " Total Expense is Rs. "+sumExpense+"/-. "
					+ "Advance given: Rs. "+sumAdvanceGiven+"/-. "
					+ "Advance taken: Rs. "+sumAdvanceTaken+"/-"
					+"\n\n";
			counter++;
		}



	}


	private Map<Integer, ExpenseUserVO> populateAllExpenseDetailForGroup(){
		cursor = splitShareDB.getGroupWiseExpenseReport(groupId);
		int counter = 1;
		Map<Integer, ExpenseUserVO> estimateUserExpenseMap = new HashMap<Integer, ExpenseUserVO>();

		if(null!=cursor && cursor.moveToFirst()){
			do{
				int userId  = cursor.getInt(3);
				System.out.println("userId:: "+userId);
				String status = cursor.getString(9);
				int amount = cursor.getInt(4);
				int receiverId = cursor.getInt(6);

				if(CommonConstant.EXPENSE_STATUS_EXPENSE.equals(status))
				{
					totalAmount = totalAmount + amount;
				}

				populateUserExpenseVO(estimateUserExpenseMap, userId, status, amount, receiverId);


				//Populate all expense list detail
				ExpenseVO expenseVO = new ExpenseVO();
				expenseVO.setExpenseId(cursor.getInt(0));
				expenseVO.setGroupId(cursor.getInt(1));
				expenseVO.setCreateName(cursor.getString(2));
				expenseVO.setUserId(userId);
				expenseVO.setAmount(amount);
				expenseVO.setExpenseDescription(cursor.getString(5));
				expenseVO.setReceiverId(cursor.getInt(6));
				expenseVO.setCreateDate(cursor.getString(7));
				expenseVO.setStatus(status);
				if(CommonConstant.EXPENSE_STATUS_EXPENSE.equals(status))
					expenseVO.setStatusDescription("Expense");
				if(CommonConstant.EXPENSE_STATUS_ADVANCE.equals(status))
				{
					String receiverName = getReceiverName(cursor.getInt(6));
					expenseVO.setStatusDescription("Advance to "+ receiverName);
				}
				reportDetailText = reportDetailText + counter +") " 
						+expenseVO.getCreateName()+"\n"
						+"Expense Description: "
						+expenseVO.getExpenseDescription()+"\n"
						+"Amount: Rs. "
						+expenseVO.getAmount()+"/-\n"
						+"Date: "
						+expenseVO.getCreateDate()+"\n"
						+"Expense Type: "
						+expenseVO.getStatusDescription()
						+"\n\n";
				counter ++;

			}while(cursor.moveToNext());
		}else{
			sendMail.setVisibility(View.GONE);
			Toast toast = Toast.makeText(ViewReportActivity.this, "No expense detail available", Toast.LENGTH_SHORT);
			toast.show();
		}

		reportSummaryCostText = "* Total expense is Rs. "+totalAmount+"/-\n";

		System.out.println("before estimateUserExpenseMap:: "+estimateUserExpenseMap);
		//Populating other user who has not contributed any expense
		postProcessExpenseMap(estimateUserExpenseMap);
		System.out.println("after estimateUserExpenseMap:: "+estimateUserExpenseMap);

		return estimateUserExpenseMap;
	}

	private void postProcessExpenseMap(Map<Integer, ExpenseUserVO> estimateUserExpenseMap) {
		Iterator<Integer> iterator = groupMemberMap.keySet().iterator();
		while(iterator.hasNext()){

			int userId = iterator.next();
			if(!estimateUserExpenseMap.containsKey(userId)){
				ExpenseUserVO expenseUserVO = new ExpenseUserVO();
				expenseUserVO.setUserId(userId);
				expenseUserVO.setUserName(groupMemberMap.get(userId).getName());
				expenseUserVO.setTotalExpenseDone(CommonConstant.ZERO);
				expenseUserVO.setAdvanceGiven(CommonConstant.ZERO);
				expenseUserVO.setAdvanceTaken(CommonConstant.ZERO);
				expenseUserVO.setPerHeadCost(CommonConstant.ZERO);
				expenseUserVO.setBalance(CommonConstant.ZERO);
				estimateUserExpenseMap.put(userId, expenseUserVO);
			}
		}
	}

	/*private void sqlTest() {
		Cursor result = splitShareDB.dummy(groupId);
		if(null!=result && result.moveToFirst()){
			do{
				System.out.println("eito eseche:: "+result.getString(0)+" ; "+result.getInt(1));
			}while(result.moveToNext());
		}
	}*/

	private void populateUserExpenseVO(Map<Integer, ExpenseUserVO> estimateUserExpenseMap,
			int userId, String status, int amount, int receiverId) {
		if(estimateUserExpenseMap.containsKey(userId)){
			ExpenseUserVO expenseUserVO = estimateUserExpenseMap.get(userId);
			if(CommonConstant.EXPENSE_STATUS_EXPENSE.equals(status)){
				expenseUserVO.setTotalExpenseDone(expenseUserVO.getTotalExpenseDone() + amount);
			}
			if(CommonConstant.EXPENSE_STATUS_ADVANCE.equals(status)){
				expenseUserVO.setAdvanceGiven(expenseUserVO.getAdvanceGiven() + amount);
				popuateUserExpenseVOForAdvanceTaken(estimateUserExpenseMap,amount, receiverId);
			}
			expenseUserVO.setBalance(expenseUserVO.getPerHeadCost() - (expenseUserVO.getTotalExpenseDone() + expenseUserVO.getAdvanceGiven() - expenseUserVO.getAdvanceTaken()));

		}else{
			ExpenseUserVO expenseUserVO = new ExpenseUserVO();
			expenseUserVO.setUserId(userId);
			expenseUserVO.setUserName(cursor.getString(2));

			if(CommonConstant.EXPENSE_STATUS_EXPENSE.equals(status)){
				expenseUserVO.setTotalExpenseDone(amount);
			}

			if(CommonConstant.EXPENSE_STATUS_ADVANCE.equals(status)){
				expenseUserVO.setAdvanceGiven(amount);
				popuateUserExpenseVOForAdvanceTaken(estimateUserExpenseMap,amount, receiverId);
			}
			expenseUserVO.setBalance(expenseUserVO.getPerHeadCost() - (expenseUserVO.getTotalExpenseDone() + expenseUserVO.getAdvanceGiven() - expenseUserVO.getAdvanceTaken()));

			estimateUserExpenseMap.put(userId, expenseUserVO);
		}
	}

	private void popuateUserExpenseVOForAdvanceTaken(Map<Integer, ExpenseUserVO> estimateMap, int amount, int receiverId) {
		if(estimateMap.containsKey(receiverId)){
			ExpenseUserVO advanceReceiverVO = estimateMap.get(receiverId);
			advanceReceiverVO.setAdvanceTaken(advanceReceiverVO.getAdvanceTaken() + amount);

		}else{
			ExpenseUserVO advanceReceiverVO = new ExpenseUserVO();
			advanceReceiverVO.setUserId(receiverId);
			advanceReceiverVO.setUserName(getReceiverName(receiverId));
			advanceReceiverVO.setAdvanceTaken(amount);

			estimateMap.put(receiverId, advanceReceiverVO);
		}
	}


	private String getReceiverName(int receiverId) {

		String receiverName="";
		Cursor result=null;
		try{
			result = splitShareDB.getUserDetailByUserId(receiverId);
			if(null!=result && result.moveToFirst())
			{
				receiverName = result.getString(1);
			}
		}catch(Exception e)
		{

		}finally{
			splitShareDB.closeDBConnection(result);

		}
		return receiverName;
	}


	private Map<Integer, UserVO> getGroupMember(int groupId) {

		Map<Integer,UserVO> map = new HashMap<Integer,UserVO>();
		cursor = splitShareDB.getUserListByGroup(groupId);
		if(null!=cursor && cursor.moveToFirst())
		{
			do{
				int userId = cursor.getInt(0);
				UserVO userVO = new UserVO();
				userVO.setUserId(userId);
				userVO.setName(cursor.getString(1));
				map.put(userId, userVO);
			}while(cursor.moveToNext());
		}
		System.out.println("All user fetched:: "+map);
		return map;
	}

	View.OnClickListener getOnClickMailContent(final int selection, final CheckBox checkBox)
	{ 
		return new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				if(checkBox.isChecked())
				{
					if(CommonConstant.SELECTION_REPORT_SUMMARY == selection){
						mailContentMap.put(selection, reportSummaryText);
					}else if(CommonConstant.SELECTION_BALANCE_REPORT_SUMMARY == selection)
						mailContentMap.put(selection, balanceSummaryText);
					else if(CommonConstant.SELECTION_INDIVIDUAL_REPORT_SUMMARY == selection){
						mailContentMap.put(selection, individualSummaryText);
					}else if(CommonConstant.SELECTION_EXPENSE_DETAIL_REPORT == selection){
						mailContentMap.put(selection, reportDetailText);
					}

				}else if(!checkBox.isChecked()){
					mailContentMap.remove(selection);
				}


			} 
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_report, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//Intent intent = new Intent(getBaseContext(), ViewGroupActivity.class);
		Intent intent = new Intent(getBaseContext(), AdminActivity.class);
		intent.putExtra(CommonConstant.USER_SESSIONVO, userSessionVO);
		intent.putExtra(CommonConstant.TRANSFER_GROUP_ID, groupId);
		startActivity(intent);

	}

}
