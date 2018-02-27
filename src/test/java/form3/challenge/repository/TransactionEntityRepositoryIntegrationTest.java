package form3.challenge.repository;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import form3.challenge.Application;
import form3.challenge.controller.dto.Transaction;
import form3.challenge.repository.domain.TransactionEntity;
import form3.challenge.service.TransactionAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static form3.challenge.TestConstants.*;
import static form3.challenge.TestUtil.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionEntityRepositoryIntegrationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private Session session;
    private Mapper<TransactionEntity> mapper;

    @Before
    public void setup() {
        MappingManager mappingManager = new MappingManager(session);
        mapper = mappingManager.mapper(TransactionEntity.class);
    }

    @After
    public void cleanup() {
        session.execute("truncate transaction");
    }

    @Test
    public void testSaveTransaction() {
        final TransactionEntity transactionIn = createTransactionEntity(TRANSACTION_ID_0, ORGANISATION_ID_0, TRANSACTION_TYPE, VERSION);

        transactionRepository.save(transactionIn);

        TransactionEntity transactionOut = mapper.get(TRANSACTION_ID_0);

        assertThat(transactionOut).isEqualTo(transactionIn);
    }

    @Test
    public void testSaveTransactions() {
        TransactionEntity transactionEntity1 = createTransactionEntity(TRANSACTION_ID_0, ORGANISATION_ID_0, TRANSACTION_TYPE, VERSION);
        TransactionEntity transactionEntity2 = createTransactionEntity(TRANSACTION_ID_0, ORGANISATION_ID_0, TRANSACTION_TYPE, VERSION);
        transactionRepository.save(asList(transactionEntity1, transactionEntity2));

        TransactionEntity transaction1Out = mapper.get(transactionEntity1.getId());
        TransactionEntity transaction2Out = mapper.get(transactionEntity2.getId());
        assertThat(transaction1Out).isEqualTo(transactionEntity1);
        assertThat(transaction2Out).isEqualTo(transactionEntity2);
    }

    @Test
    public void testUpdateTransaction() throws IOException {
        final Transaction transaction = createTransaction(TRANSACTION_ID_0, ORGANISATION_ID_0, TRANSACTION_TYPE, VERSION);
        transactionRepository.save(TransactionAdapter.of(transaction));

        UUID updatedOrgid = UUID.randomUUID();
        transaction.setOrganisation_id(updatedOrgid);

        transactionRepository.save(TransactionAdapter.of(transaction));

        TransactionEntity transactionOut = mapper.get(TRANSACTION_ID_0);

        assertThat(TransactionAdapter.of(transactionOut)).isNotEqualTo(createTransaction());
        assertThat(TransactionAdapter.of(transactionOut).getOrganisation_id()).isEqualTo(updatedOrgid);
    }

    @Test
    public void testDeleteTransaction() {
        List<TransactionEntity> transactions = saveAndGetTwoTransactions();

        transactionRepository.delete(transactions.get(0).getId());
        assertThat(mapper.get(transactions.get(0).getId())).isNull();
        assertThat(countTransactions(session)).isEqualTo(1);
    }

    @Test
    public void testDeleteTransactions() {
        List<TransactionEntity> transactions = saveAndGetTwoTransactions();

        transactionRepository.delete(transactions.stream().map(t -> t.getId()).collect(Collectors.toSet()));
        assertThat(countTransactions(session)).isEqualTo(0);
    }

    private List<TransactionEntity> saveAndGetTwoTransactions() {
        List<TransactionEntity> transactions = asList(
              TRANSACTION_ENTITY_0, TRANSACTION_ENTITY_1
        );
        transactions.forEach(i -> transactionRepository.save(i));

        assertThat(countTransactions(session)).isEqualTo(2);
        return transactions;
    }
}