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

import br.com.app.bancostout.model.BankAccount;
import br.com.app.bancostout.model.Costumer;
import br.com.app.bancostout.service.FirebaseService;

@Repository
public class FireBaseCostumerRepo implements CostumerRepository {

	@SuppressWarnings("unused")
	private final FirebaseService firebaseService;

	@Autowired
	public FireBaseCostumerRepo(FirebaseService firebaseService) {
		this.firebaseService = firebaseService;
	}

	Firestore db = FirestoreClient.getFirestore();
	String costumersCollection = "Costumers";
	String accountsCollection = "CostumerAccounts";

	@Override
	public Optional<Costumer> loadByUsername(String username) {
		Query query = db.collection(costumersCollection).whereEqualTo("username", username).limit(1);
		ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();

		try {
			DocumentSnapshot documentSnapshot = querySnapshotFuture.get().getDocuments().get(0);
			return Optional.ofNullable(documentSnapshot.toObject(Costumer.class));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(null);
	}

	@Override
	public Object save(Costumer costumer) {
		try {
			db.collection(costumersCollection).add(costumer).get();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return costumer;
	}

	@Override
	public boolean createNewBankAccount(Costumer costumer, BankAccount bankAccount) {

		if (doesAccountExists(bankAccount))
			return false;

		costumer.getId().collection(accountsCollection).add(bankAccount);
		return true;
	}

	@Override
	public boolean doesAccountExists(BankAccount account) {

		try {

			return !db.collectionGroup(accountsCollection).whereEqualTo("accountType", account.getAccountType()).get()
					.get().isEmpty();

		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
			return true;
		}
	}

}
