package br.com.app.bancostout.service;

import org.springframework.stereotype.Service;

import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;

@Service
public interface BankAccountService {
	
	long requestBalance(int accountNumber);
	boolean requestDeposit(int accountNumber, long depositAmmount);
	boolean requestWitdraw(int accountNumber, long withdrawAmmount);
	boolean requestTransfer(int accountNumber ,long transferAmmount, int destinedAccount);
	int generateAccountNumber(AccountType accountType);
	BankAccount generateNewBankAccount(AccountType accountType);
	boolean doesAccountExist(int accountNumber, AccountType accountType);
}
