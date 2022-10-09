package br.com.app.bancostout.DAO;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import br.com.app.bancostout.model.AccountNotFoundException;
import br.com.app.bancostout.model.AccountType;
import br.com.app.bancostout.model.BankAccount;
import br.com.app.bancostout.service.FirebaseService;

@Repository
public class FirebaseBankAccountRepo implements BankAccountRepository {

	@SuppressWarnings("unused")
	private final FirebaseService firebaseService;

	@Autowired
	public FirebaseBankAccountRepo(FirebaseService firebaseService) {
		this.firebaseService = firebaseService;
	}

	Firestore db = FirestoreClient.getFirestore();
	String accountsCollection = "CostumerAccounts";

	@Override
	public BankAccount getByAccountNumber(int accountNumber, AccountType accountType) throws AccountNotFoundException {
		Query query = db.collectionGroup(accountsCollection).whereEqualTo("accountType", accountType)
				.whereEqualTo("accountNumber", accountNumber).limit(1);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		try {
			if(querySnapshot.get().getDocuments().isEmpty()) throw new AccountNotFoundException("Account not found: " + accountNumber);
			DocumentSnapshot documentSnapshot = querySnapshot.get().getDocuments().get(0);
			return documentSnapshot.toObject(BankAccount.class);
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void save(BankAccount account) {
		account.getId().set(account);
	}
}
