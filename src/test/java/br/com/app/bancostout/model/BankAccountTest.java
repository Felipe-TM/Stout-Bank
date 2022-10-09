package br.com.app.bancostout.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BankAccountTest {

	BankAccount bankAccount1;
	BankAccount bankAccount2;

	@BeforeEach
	void createAccounts() {
		bankAccount1 = new BankAccount(12345, AccountType.CHECKING_ACCOUNT);
		bankAccount2 = new BankAccount(12345, AccountType.SAVINGS_ACCOUNT);
		bankAccount1.deposit(500L);
		bankAccount2.deposit(500L);
	}

	@ParameterizedTest()
	@ValueSource(longs = { -50L, 0L, 5L, 90L, 5000L, 90000000000000L })
	void testDeposit(long depositAmount) {

		assumingThat(depositAmount > 0, () -> {

			assertEquals(500 + depositAmount, bankAccount1.deposit(depositAmount));
			assertEquals(500 + depositAmount, 	bankAccount2.deposit(depositAmount));

		});

		assumingThat(depositAmount <= 0, () -> {

			assertThrows(IllegalArgumentException.class, () -> bankAccount1.deposit(depositAmount));
			assertThrows(IllegalArgumentException.class, () -> bankAccount2.deposit(depositAmount));
		});
	}

	@ParameterizedTest()
	@ValueSource(longs = { -900L, 0L, 60L, 500L, 600L, 9000L })
	void testWithdraw(long withdrawAmount) {

		assumingThat(withdrawAmount > 0 && withdrawAmount <= 500, () -> {

			assertEquals(500 - withdrawAmount, bankAccount1.withdraw(withdrawAmount));
			assertEquals(500 - withdrawAmount, bankAccount2.withdraw(withdrawAmount));

		});

		assumingThat(withdrawAmount <= 0 || withdrawAmount > 500, () -> {

			assertThrows(IllegalArgumentException.class, () -> bankAccount1.withdraw(withdrawAmount));
			assertThrows(IllegalArgumentException.class, () -> bankAccount2.withdraw(withdrawAmount));
		});
	}

	@ParameterizedTest()
	@ValueSource(longs = { -900L, 0L, 50L, 500L, 9000L })
	void testTransfer(long transferAmount) {

		assumingThat(transferAmount > 0 && transferAmount <= 500, () -> {

		assertEquals(500 - transferAmount, bankAccount1.transfer(transferAmount, bankAccount2));
		assertEquals(500 + transferAmount, bankAccount2.getAccountBalance());
		
		});

		assumingThat(transferAmount <= 0 || transferAmount > 500, () -> {
			
			assertThrows(IllegalArgumentException.class, () -> bankAccount1.transfer(transferAmount, bankAccount2));
			assertThrows(IllegalArgumentException.class, () -> bankAccount2.transfer(transferAmount, bankAccount1));
		});
	}
}
