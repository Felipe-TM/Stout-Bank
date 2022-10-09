package br.com.app.bancostout.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.app.bancostout.DAO.FirebaseBankAccountRepo;
import br.com.app.bancostout.model.AccountNotFoundException;
import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;

@SpringBootTest
class BankingAccountServiceTest {

	@Autowired
	BankingAccountService bankingAccountService;

	@MockBean
	FirebaseBankAccountRepo mockRepository;

	BankAccount bankAccount1;
	BankAccount bankAccount2;

	@BeforeEach
	void setUp() {
		bankAccount1 = new BankAccount(123455, AccountType.CHECKING_ACCOUNT);
		bankAccount2 = new BankAccount(543213, AccountType.SAVINGS_ACCOUNT);
		bankAccount1.deposit(3000);
		bankAccount2.deposit(3000);
		Mockito.when(mockRepository.getByAccountNumber(12345, AccountType.CHECKING_ACCOUNT)).thenReturn(bankAccount1);
		Mockito.when(mockRepository.getByAccountNumber(54321, AccountType.SAVINGS_ACCOUNT)).thenReturn(bankAccount2);
	}

	@Test
	void testRequestBalance() {

		assertEquals(3000L, bankingAccountService.requestBalance(123455));
		assertEquals(3000L, bankingAccountService.requestBalance(543213));

	}

	@ParameterizedTest
	@ValueSource(longs = { -1000, -50, 0, 2, 400, 5000 })
	void testRequestDeposit(long depositAmount) {

		assumingThat(depositAmount <= 0L, () -> {
			assertEquals(false, bankingAccountService.requestDeposit(123455, depositAmount));
			assertEquals(false, bankingAccountService.requestDeposit(543213, depositAmount));
		});

		assumingThat(depositAmount > 0L && depositAmount <= 3000L, () -> {
			bankingAccountService.requestDeposit(123455, depositAmount);
			bankingAccountService.requestDeposit(543213, depositAmount);
			assertEquals(3000L + depositAmount, bankingAccountService.requestBalance(123455));
			assertEquals(3000L + depositAmount, bankingAccountService.requestBalance(543213));
		});

	}

	@ParameterizedTest
	@ValueSource(longs = { -1000, -50, 0, 2, 400, 5000 })
	void testRequestWithdraw(long withdrawAmount) {

		assumingThat(withdrawAmount <= 0L || withdrawAmount > 3000, () -> {
			assertEquals(false, bankingAccountService.requestWitdraw(123455, withdrawAmount));
			assertEquals(false, bankingAccountService.requestWitdraw(543213, withdrawAmount));
		});

		assumingThat(withdrawAmount > 0L && withdrawAmount <= 3000L, () -> {
			bankingAccountService.requestWitdraw(123455, withdrawAmount);
			bankingAccountService.requestWitdraw(543213, withdrawAmount);
			assertEquals(3000L - withdrawAmount, bankingAccountService.requestBalance(123455));
			assertEquals(3000L - withdrawAmount, bankingAccountService.requestBalance(543213));
		});
	}

	@ParameterizedTest
	@ValueSource(longs = { -1000, -50, 0, 2, 400, 5000 })
	void testRequestTransfer(long transferAmount) {

		assumingThat(transferAmount <= 0L || transferAmount > 3000, () -> {
			assertEquals(false, bankingAccountService.requestTransfer(123455, transferAmount, 543213));
			assertEquals(false, bankingAccountService.requestTransfer(543213, transferAmount, 123455));
		});

		assumingThat(transferAmount > 0L && transferAmount <= 3000L, () -> {

			bankingAccountService.requestTransfer(123455, transferAmount, 543213);
			assertEquals(3000L - transferAmount, bankingAccountService.requestBalance(123455));
			assertEquals(3000L + transferAmount, bankingAccountService.requestBalance(543213));

			bankingAccountService.requestTransfer(543213, transferAmount, 123455);
			assertEquals(3000L, bankingAccountService.requestBalance(543213));
			assertEquals(3000L, bankingAccountService.requestBalance(123455));
		});
	}

	
	@RepeatedTest(5)
	void testGenerateAccountNumber() {
		
		Mockito.when(mockRepository.getByAccountNumber(anyInt(), any(AccountType.class)))
		.thenThrow(AccountNotFoundException.class);
		
		Integer accountNumber = (Integer) bankingAccountService.generateAccountNumber(AccountType.CHECKING_ACCOUNT);
		
		assertEquals(5 ,accountNumber.toString().length());
	}

	@Test
	void testDoesAccountExist() {

		Mockito.when(mockRepository.getByAccountNumber(98767, AccountType.CHECKING_ACCOUNT))
				.thenThrow(AccountNotFoundException.class);
		Mockito.when(mockRepository.getByAccountNumber(32165, AccountType.SAVINGS_ACCOUNT))
				.thenThrow(AccountNotFoundException.class);

		assertEquals(false, bankingAccountService.doesAccountExist(98767, AccountType.CHECKING_ACCOUNT));
		assertEquals(false, bankingAccountService.doesAccountExist(32165, AccountType.SAVINGS_ACCOUNT));
		assertEquals(true, bankingAccountService.doesAccountExist(12345, AccountType.CHECKING_ACCOUNT));
		assertEquals(true, bankingAccountService.doesAccountExist(54321, AccountType.SAVINGS_ACCOUNT));

	}

	@ParameterizedTest
	@ValueSource(ints = { 123455, 543213, 987543, 543215, 123456, 543210, -1})
	void testGetAccountNumberType(int suffix) {

		assumingThat(!AccountType.accountTypeMapper.containsKey(suffix % 10), () -> {
			assertThrows(IllegalArgumentException.class, () -> bankingAccountService.getAccountNumberType(suffix));
		});
	}

	@Test
	void testGetAccountNumber() {
		
		assertEquals(12345, bankingAccountService.getAccountNumber(123455));
		assertEquals(54321, bankingAccountService.getAccountNumber(543213));
		
	}
}
