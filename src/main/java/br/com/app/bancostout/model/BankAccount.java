package br.com.app.bancostout.model;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;

public class BankAccount {
	
	@DocumentId
	private DocumentReference id;
	private int accountNumber;	
	private AccountType accountType;	
	private long accountBalance;

	public BankAccount() {}

	public BankAccount(int accountNumber, AccountType accountType){
		this.accountNumber = accountNumber;
		this.accountType = accountType;
	}
	
	public BankAccount(DocumentReference id, int accountNumber, AccountType accountType, long accountBalance) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
	}

	public long deposit(long depositAmmount) throws IllegalArgumentException {
		if (depositAmmount <= 0)
			throw new IllegalArgumentException(
					"Deposit ammount must not be less or equal to zero");
		this.accountBalance += depositAmmount; 
		return this.accountBalance;
	}


	public long withdraw(long withdrawAmmount) throws IllegalArgumentException {
		if (withdrawAmmount > accountBalance || withdrawAmmount <= 0)
			throw new IllegalArgumentException(
					"Withdraw ammount must not be greater than the account balance nor less or equal to zero");
		this.accountBalance -= withdrawAmmount;
		return this.accountBalance;
	}


	public long transfer(long transferAmmount, BankAccount destinedAccount) throws IllegalArgumentException {
		if (transferAmmount > accountBalance || transferAmmount <= 0)
			throw new IllegalArgumentException(
					"Transfer ammount must not be greater than the account balance nor less or equal to zero");
		this.accountBalance -= transferAmmount; 
		destinedAccount.deposit(transferAmmount);
		return this.accountBalance;
	}

	public DocumentReference getId() {
		return id;
	}


	public void setId(DocumentReference id) {
		this.id = id;
	}


	public int getAccountNumber() {
		return this.accountNumber;
	}


	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}


	public AccountType getAccountType() {
		return this.accountType;
	}


	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	public long getAccountBalance() {
		return this.accountBalance;
	}

	public int getAccountSuffix() {
		return this.accountType.getAccountSuffix();
	}
	
	
}
