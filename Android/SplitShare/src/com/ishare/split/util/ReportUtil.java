package com.ishare.split.util;

public class ReportUtil {
	
	public static String getHelp()
	{
		String help = "\tWelcome to Split-N-Share App. This app is to keep track of group expense and budget.\n\n" +
				"\tProvide the user id as admin and log in. Home screen will appear.\n\n" +
				"\t1. Create Group: Click on ADD GROUP icon and create group. Provide group name, description and estimated budget.\n\n" +
				"\t2. Add User: Click on ADD USER and provide the user name and other information as asked(optional). This will create user to this application.\n\n" +
				"\t3. View Group: Click on VIEW GROUP icon and select one group from radio list. All group details will appear.\n\n" +
				"\t\t3.1. Click on UPDATE GROUP to update the group information.\n\n" +
				"\t\t3.2. Click on ADD MEMBER and select multiple existing members to add to this group. Deactivated members can not be added.\n\n" +
				"\t\t3.3. Click on VIEW MEMBER to see the user(s) attached to this group.\n\n" +
				"\t\t3.4. Click on ADD EXPENSE and select one group member to add the expense details for this user.\n\n" +
				"\t\t\t3.4.1. Provide the expense details. If this user has made any outgoing expense the do not check the checkbox.\n\n" +
				"\t\t\t3.4.2. Click this check box only the member has made some advance payment to other group member only. In that case please select the group member name to whom advance money has been given.\n\n" +
				"\t\t3.5. Click on Expense history to see the all the expense history trail for this group till date. This expense entry can be modified as well.\n\n" +
				"\t\t3.6. Click on DELETE GROUP to delete this group.\n\n" +
				"\t4. Click on VIEW USER icon to see the existing users to this app. This user(s) can be deleted or deactivated.\n\n" +
				"\t5. Click on REPORT and select the group. It will display the all the expense history and the balance sheet for this group.\n\n" +
				"\t6. Click on HELP icon to see the help.\n\n" +
				"\t7. Click on HOME icon above the screen on the right side to go to the home screen.\n\n" +
				"\t8. Click on log out button to log out.\n\n";
		return help;
	}
}
