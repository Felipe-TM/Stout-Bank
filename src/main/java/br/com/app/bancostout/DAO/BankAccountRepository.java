package br.com.app.bancostout.DAO;


import java.util.Optional;

import br.com.app.bancostout.model.AccountNotFoundException;
import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;

public interface BankAccountRepository{
	
	
	public BankAccount getByAccountNumber(int accountNumber, AccountType accountType) throws AccountNotFoundException;
	public void save(BankAccount account);

}
