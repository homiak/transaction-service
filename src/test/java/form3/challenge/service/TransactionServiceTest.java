package form3.challenge.service;

import form3.challenge.repository.TransactionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static form3.challenge.TestConstants.TRANSACTION_0;
import static form3.challenge.TestConstants.TRANSACTION_ENTITY_0;
import static form3.challenge.TestConstants.TRANSACTION_ID_0;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

// TODO complete the tests
@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @Before
    public void setUp()  {
        this.transactionService = new TransactionService(transactionRepository);
    }

    @After
    public void tearDown()  {
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    public void getTransaction() {
        when(transactionRepository.get(TRANSACTION_ID_0)).thenReturn(Optional.of(TRANSACTION_ENTITY_0));
        assertThat(transactionService.getTransaction(TRANSACTION_ID_0).get()).isEqualTo(TRANSACTION_0);
        verify(transactionRepository).get(TRANSACTION_ID_0);
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
    public void validateTransaction() {
    }

    @Test
    public void validateTransactions() {
    }
}