package com.ishare.split.vo;

public class ExpenseUserVO extends ExpenseVO{
	
	private int userExpenseId;
	private int advanceGiven;
	private int advanceTaken;
	private int totalExpenseDone;
	private int perHeadCost;
	private int balance;

	public int getUserExpenseId() {
		return userExpenseId;
	}

	public void setUserExpenseId(int userExpenseId) {
		this.userExpenseId = userExpenseId;
	}

	public int getAdvanceGiven() {
		return advanceGiven;
	}

	public void setAdvanceGiven(int advanceGiven) {
		this.advanceGiven = advanceGiven;
	}

	public int getAdvanceTaken() {
		return advanceTaken;
	}

	public void setAdvanceTaken(int advanceTaken) {
		this.advanceTaken = advanceTaken;
	}

	public int getTotalExpenseDone() {
		return totalExpenseDone;
	}

	public void setTotalExpenseDone(int totalExpenseDone) {
		this.totalExpenseDone = totalExpenseDone;
	}

	public int getPerHeadCost() {
		return perHeadCost;
	}

	public void setPerHeadCost(int perHeadCost) {
		this.perHeadCost = perHeadCost;
	}
	
	
	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
	
		return "[perHeadCost="+perHeadCost
				+", totalExpenseDone="+totalExpenseDone
				+", advanceTaken="+advanceTaken
				+", advanceGiven="+advanceGiven
				+", balance="+balance+"]";
	}
	
	
}
