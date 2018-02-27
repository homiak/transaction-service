package form3.challenge;

import form3.challenge.controller.dto.Transaction;
import form3.challenge.controller.dto.Transactions;
import form3.challenge.repository.domain.TransactionEntity;
import form3.challenge.service.TransactionAdapter;

import java.util.UUID;

import static form3.challenge.TestUtil.createTransaction;
import static java.util.Arrays.asList;

public class TestConstants {

    public static final UUID TRANSACTION_ID_0 = new UUID(0, 0);
    public static final UUID TRANSACTION_ID_2 = new UUID(1, 0);
    public static final UUID ORGANISATION_ID_0 = UUID.randomUUID();
    public static final String TRANSACTION_TYPE = "Payment";
    public static final int VERSION = 0;

    public static final UUID TRANSACTION_ID_1 = UUID.fromString("4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43");
    public static final UUID ORGANISATION_ID_1 = UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb");

    public static final Transaction TRANSACTION_0 = createTransaction(TRANSACTION_ID_0, ORGANISATION_ID_0, TRANSACTION_TYPE, VERSION);
    public static final Transaction TRANSACTION_1 = createTransaction(TRANSACTION_ID_1, ORGANISATION_ID_1, TRANSACTION_TYPE, VERSION);

    public static final TransactionEntity TRANSACTION_ENTITY_0 = TransactionAdapter.of(TRANSACTION_0);
    public static final TransactionEntity TRANSACTION_ENTITY_1 = TransactionAdapter.of(TRANSACTION_1);
    public static final Transactions TRANSACTIONS = new Transactions(asList(TRANSACTION_0, TRANSACTION_1));


    private TestConstants() {
    }
}
