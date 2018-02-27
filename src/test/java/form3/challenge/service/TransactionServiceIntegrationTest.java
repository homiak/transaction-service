package form3.challenge.service;

import form3.challenge.Application;
import form3.challenge.repository.TransactionRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static form3.challenge.TestConstants.TRANSACTION_0;
import static form3.challenge.TestConstants.TRANSACTION_ENTITY_0;
import static form3.challenge.TestConstants.TRANSACTION_ID_0;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

// TODO complete the tests
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = RANDOM_PORT)
public class TransactionServiceIntegrationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @Test
    public void getTransaction() {
        transactionRepository.save(TRANSACTION_ENTITY_0);
        assertThat(transactionService.getTransaction(TRANSACTION_ID_0).get()).isEqualTo(TRANSACTION_0);
    }

    @Test
    public void listTransactions() {
    }

    @Test
    public void updateTransaction() {
    }

    @Test
    public void saveTransaction() {
    }

    @Test
    public void saveTransactions() {
    }

    @Test
    public void deleteTransaction() {
    }

    @Test
    public void deleteTransactions() {
    }

    @Test
    public void validateTransactions() {
    }

    @Test
    public void validateTransaction() {
    }
}