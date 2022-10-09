package br.com.app.bancostout.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.app.bancostout.DAO.BankAccountRepository;
import br.com.app.bancostout.DAO.FirebaseBankAccountRepo;
import br.com.app.bancostout.model.AccountNotFoundException;
import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;

@Service
public class BankingAccountService implements BankAccountService {

	private BankAccountRepository accountRepository;

	@Autowired
	public BankingAccountService(FirebaseBankAccountRepo accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public long requestBalance(int accountNumber) {

		try {

			BankAccount costumerAccount = accountRepository.getByAccountNumber(getAccountNumber(accountNumber),
					getAccountNumberType(accountNumber));

			return costumerAccount.getAccountBalance();

		} catch (AccountNotFoundException e) {
			//System.out.println(e.getMessage());
		}

		return 0;
	}

	@Override
	public boolean requestDeposit(int accountNumber, long depositAmmount)
			throws IllegalArgumentException, AccountNotFoundException {

		BankAccount costumerAccount = accountRepository.getByAccountNumber(getAccountNumber(accountNumber),
				getAccountNumberType(accountNumber));

		try {
			costumerAccount.deposit(depositAmmount);
			accountRepository.save(costumerAccount);
			return true;
			
		} catch (IllegalArgumentException e) {
			//System.out.println(e.getMessage());
			return false;

		} catch (AccountNotFoundException e) {
			//System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean requestWitdraw(int accountNumber, long withdrawAmmount) {

		BankAccount costumerAccount = accountRepository.getByAccountNumber(getAccountNumber(accountNumber),
				getAccountNumberType(accountNumber));

		try {
			costumerAccount.withdraw(withdrawAmmount);
			accountRepository.save(costumerAccount);
			return true;

		} catch (IllegalArgumentException e) {
			//System.out.println(e.getMessage());
			return false;

		} catch (AccountNotFoundException e) {
			//System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean requestTransfer(int accountNumber, long transferAmmount, int destinedAccountNumber)
			throws IllegalArgumentException {

		BankAccount costumerAccount = accountRepository.getByAccountNumber(getAccountNumber(accountNumber),
				getAccountNumberType(accountNumber));

		BankAccount destinedAccount = accountRepository.getByAccountNumber(getAccountNumber(destinedAccountNumber),
				getAccountNumberType(destinedAccountNumber));

		try {
			costumerAccount.transfer(transferAmmount, destinedAccount);
			accountRepository.save(costumerAccount);
			accountRepository.save(destinedAccount);
			return true;

		} catch (IllegalArgumentException e) {
			//System.out.println(e.getMessage());
			return false;

		} catch (AccountNotFoundException e) {
			//System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public int generateAccountNumber(AccountType accountType) {

		final int max = 99999;
		final int min = 10000;
		Random random = new Random();
		int number = random.ints(min, max).findFirst().getAsInt();

		do {
			number = random.ints(min, max).findFirst().getAsInt();
		} while (doesAccountExist(number, accountType));
		return number;
	}

	@Override
	public boolean doesAccountExist(int accountNumber, AccountType accountType) {
		try {
			accountRepository.getByAccountNumber(accountNumber, accountType);
			return true;
		} catch (AccountNotFoundException e) {
			return false;
		}
		
		
	
	}

	@Override
	public BankAccount generateNewBankAccount(AccountType accountType) {
		return new BankAccount(generateAccountNumber(accountType), accountType);
	}

	public AccountType getAccountNumberType(int accountNumber) {
		return AccountType.getAccountType(accountNumber % 10);
	}

	public int getAccountNumber(int accountNumber) {
		return (accountNumber - (accountNumber % 10)) / 10;
	}

}
