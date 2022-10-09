package br.com.app.bancostout.DAO;

import java.util.Optional;

import br.com.app.bancostout.model.BankAccount;
import br.com.app.bancostout.model.Costumer;

public interface CostumerRepository{
	
	Optional<Costumer> loadByUsername(String usernames);
	Object save(Costumer costumer);
	boolean createNewBankAccount(Costumer costumer, BankAccount bankAccount);
	boolean doesAccountExists(BankAccount account);
}
