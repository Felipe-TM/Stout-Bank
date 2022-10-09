package br.com.app.bancostout.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.api.gax.rpc.InvalidArgumentException;

public enum AccountType {
	SAVINGS_ACCOUNT(3),
	CHECKING_ACCOUNT(5);
	
	private int accountSuffix;
	public static Map<Integer, AccountType> accountTypeMapper = Arrays.stream(values())
            .collect(Collectors.toMap(AccountType::getAccountSuffix, Function.identity()));
	
	AccountType(int i) {
		this.accountSuffix = i;
	}
	
	public int getAccountSuffix() {
		return accountSuffix;
	}
	
	public static AccountType getAccountType(int suffix) throws IllegalArgumentException{
		if(!accountTypeMapper.containsKey(suffix)) throw new IllegalArgumentException(suffix + " isn't a valid account suffix");
		return accountTypeMapper.get(suffix);
	}
}
