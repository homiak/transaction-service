package form3.challenge.service;

import form3.challenge.TestConstants;
import form3.challenge.controller.dto.Transaction;
import form3.challenge.repository.domain.TransactionEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static form3.challenge.TestConstants.TRANSACTION_0;
import static form3.challenge.TestConstants.TRANSACTION_ENTITY_0;
import static form3.challenge.TestUtil.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionAdapterTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void ofTransactionEntity() throws Exception {
        Transaction transaction = TransactionAdapter.of(TRANSACTION_ENTITY_0);
        assertThat(transaction.getId()).isEqualTo(TRANSACTION_ENTITY_0.getId());
        assertThat(transaction.getOrganisation_id()).isEqualTo(TRANSACTION_ENTITY_0.getOrganisation_id());
        assertThat(transaction.getType()).isEqualTo(TRANSACTION_ENTITY_0.getType());
        assertThat(transaction.getVersion()).isEqualTo(TRANSACTION_ENTITY_0.getVersion());
        assertThat(transaction).isEqualTo(MAPPER.readValue(TRANSACTION_ENTITY_0.getTransaction_json(), Transaction.class));
    }

    @Test
    public void ofTransaction() throws Exception {
        TransactionEntity transactionEntity = TransactionAdapter.of(TRANSACTION_0);
        assertThat(transactionEntity.getId()).isEqualTo(TRANSACTION_0.getId());
        assertThat(transactionEntity.getOrganisation_id()).isEqualTo(TRANSACTION_0.getOrganisation_id());
        assertThat(transactionEntity.getType()).isEqualTo(TRANSACTION_0.getType());
        assertThat(transactionEntity.getVersion()).isEqualTo(TRANSACTION_0.getVersion());
        assertThat(MAPPER.readValue(transactionEntity.getTransaction_json(), Transaction.class)).isEqualTo(TRANSACTION_0);
    }
}