package form3.challenge.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import form3.challenge.controller.dto.Transaction;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URL;

import static form3.challenge.TestUtil.MAPPER;
import static form3.challenge.TestUtil.createTransaction;
import static org.assertj.core.api.Assertions.assertThat;

public class JavaTimeAndMoneyAwareObjectMapperTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldBeAbleToSerializeDeserializeTransaction() throws IOException {
        Transaction transaction = createTransaction();
        String transactionAsString = MAPPER.writeValueAsString(transaction);
        Transaction readTransaction = MAPPER.readValue(transactionAsString, Transaction.class);
        assertThat(transaction).isEqualTo(readTransaction);
    }

    @Test
    public void inMemoryTransactionShouldMatchGivenInFile() throws IOException {
        Transaction transaction = createTransaction();

        URL url = Resources.getResource("payment.json");
        String transactionAsString = Resources.toString(url, Charsets.UTF_8);
        Transaction readTransaction = MAPPER.readValue(transactionAsString, Transaction.class);
        assertThat(transaction).isEqualTo(readTransaction);
    }
}