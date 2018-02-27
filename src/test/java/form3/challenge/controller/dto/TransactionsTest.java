package form3.challenge.controller.dto;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;

import static form3.challenge.TestUtil.MAPPER;
import static form3.challenge.TestUtil.createTransaction;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TransactionsTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldSerializeDeserialiseTransactions() throws IOException {
        URL url = Resources.getResource("payments.json");
        String transactionAsString = Resources.toString(url, Charsets.UTF_8);
        Transactions readTransactions = MAPPER.readValue(transactionAsString, Transactions.class);
        Transaction firstTransaction = readTransactions.getData().get(0);
        assertThat(firstTransaction).isEqualTo(createTransaction());
    }
}