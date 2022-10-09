package br.com.app.bancostout.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.app.bancostout.DAO.FireBaseCostumerRepo;
import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;
import br.com.app.bancostout.model.Costumer;
import br.com.app.bancostout.model.UsernameAlreadyExistsException;

@SpringBootTest
class CostumerServiceTest {

	@Autowired
	CostumerService costumerService;

	@MockBean
	FireBaseCostumerRepo mockRepository;

	Costumer costumer;

	@BeforeEach
	void setUp() {
		costumer = new Costumer("Felipe", "Feltm", "asdadads");
		Mockito.when(mockRepository.loadByUsername(anyString())).thenReturn(Optional.ofNullable(null));
	}

	@Test
	void testLoadByUsername() {
		Mockito.when(mockRepository.loadByUsername("Feltm")).thenReturn(Optional.ofNullable(costumer));

		String loadUserByUsername = costumerService.loadUserByUsername("Feltm").getUsername();
		assertEquals("Feltm", loadUserByUsername);

	}

	@Test
	void testCreateNewCostumer() {

		Mockito.when(mockRepository.loadByUsername("Feltm")).thenReturn(Optional.ofNullable(costumer));

		assertTrue(costumerService.createNewCostumer("Felipe", "FelipeS", "adasdasd"));
		assertThrows(UsernameAlreadyExistsException.class,
				() -> costumerService.createNewCostumer("Feps", "Feltm", "asdassds"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "Feltm", "AngelaMT", "WilsonMD", "PauloTs" })
	void testDoesUsernameAlreadyExists(String username) {

		Mockito.when(mockRepository.loadByUsername("Feltm")).thenReturn(Optional.ofNullable(costumer));
		Mockito.when(mockRepository.loadByUsername("WilsonMD")).thenReturn(Optional.ofNullable(costumer));

		if (costumerService.doesUsernameAlreadyExists(username)) {
			assertTrue(costumerService.doesUsernameAlreadyExists(username));
		} else {
			assertFalse(costumerService.doesUsernameAlreadyExists(username));
		}

	}

	@Test
	void testCreateNewBankAccount() {
		Mockito.when(mockRepository.loadByUsername("Feltm")).thenReturn(Optional.ofNullable(costumer));
		Mockito.when(mockRepository.doesAccountExists(any(BankAccount.class))).thenReturn(false);

		if (!mockRepository.doesAccountExists(new BankAccount())) {
			Mockito.when(mockRepository.createNewBankAccount(any(Costumer.class), any(BankAccount.class)))
					.thenReturn(true);
			
			assertTrue(costumerService.createNewBankAccout("Feltm", AccountType.CHECKING_ACCOUNT));
		}
		
		Mockito.when(mockRepository.doesAccountExists(any(BankAccount.class))).thenReturn(true);
		
		if (mockRepository.doesAccountExists(new BankAccount())) {
			Mockito.when(mockRepository.createNewBankAccount(any(Costumer.class), any(BankAccount.class)))
					.thenReturn(false);
			
			assertFalse(costumerService.createNewBankAccout("Feltm", AccountType.CHECKING_ACCOUNT));
		}

	}
}
