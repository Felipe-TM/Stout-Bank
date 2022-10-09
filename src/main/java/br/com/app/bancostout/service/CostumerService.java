package br.com.app.bancostout.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.app.bancostout.DAO.CostumerRepository;
import br.com.app.bancostout.DAO.FireBaseCostumerRepo;
import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;
import br.com.app.bancostout.model.Costumer;
import br.com.app.bancostout.model.UsernameAlreadyExistsException;
import br.com.app.bancostout.security.PasswordEncoderConfig;

@Service
public class CostumerService implements UserDetailsService {

	private CostumerRepository costumerRepository;
	private PasswordEncoderConfig passwordEncoderConfig;
	private BankingAccountService bankingAccountService;

	@Autowired
	public CostumerService(FireBaseCostumerRepo costumerRepository, PasswordEncoderConfig passwordEncoderConfig,
			BankingAccountService bankingAccountService) {
		this.costumerRepository = costumerRepository;
		this.passwordEncoderConfig = passwordEncoderConfig;
		this.bankingAccountService = bankingAccountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Costumer costumer = costumerRepository.loadByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
		return costumer;
	}

	public boolean createNewCostumer(String name, String username, String password) {
		if (doesUsernameAlreadyExists(username))
			throw new UsernameAlreadyExistsException("Account with username " + username + " already exists.");
		String encodedPassword = passwordEncoderConfig.passwordEncoder().encode(password);
		costumerRepository.save(new Costumer(name, username, encodedPassword));
		return true;
	}

	public boolean doesUsernameAlreadyExists(String username) {
		return costumerRepository.loadByUsername(username).isPresent();
	}

	public boolean createNewBankAccout(String username, AccountType accountType) {
		Costumer costumer = costumerRepository.loadByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("That type of account already exists for given costumer"));
		BankAccount account = bankingAccountService.generateNewBankAccount(accountType);
		if (costumerRepository.doesAccountExists(account)) return false;
		return costumerRepository.createNewBankAccount(costumer, account);
	}

}
