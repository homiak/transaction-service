package form3.challenge.service;

import form3.challenge.controller.dto.Transaction;
import form3.challenge.repository.domain.TransactionEntity;

import java.io.IOException;

public class TransactionAdapter {

    private final static JavaTimeAndMoneyAwareObjectMapper MAPPER = new JavaTimeAndMoneyAwareObjectMapper();

    public static Transaction of(final TransactionEntity transactionEntity) {
       try {
           return MAPPER.readValue(transactionEntity.getTransaction_json(), Transaction.class);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    public static TransactionEntity of(final Transaction transaction) {
       try {
           return new TransactionEntity().builder()
                   .id(transaction.getId())
                   .type(transaction.getType())
                   .organisation_id(transaction.getOrganisation_id())
                   .version(transaction.getVersion())
                   .transaction_json(MAPPER.writeValueAsString(transaction))
                   .build();
       } catch(IOException e) {
           throw new RuntimeException(e);
       }
    }
}
