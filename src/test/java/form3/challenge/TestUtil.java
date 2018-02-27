package form3.challenge;

import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import form3.challenge.controller.dto.*;
import form3.challenge.repository.domain.TransactionEntity;
import form3.challenge.service.JavaTimeAndMoneyAwareObjectMapper;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static form3.challenge.TestConstants.*;
import static java.util.Arrays.asList;

public class TestUtil {

    public static final JavaTimeAndMoneyAwareObjectMapper MAPPER = new JavaTimeAndMoneyAwareObjectMapper();
    private static final CurrencyUnit GBP = Monetary.getCurrency("GBP");
    private static final CurrencyUnit USD = Monetary.getCurrency("USD");

    public static TransactionEntity createTransactionEntity(final UUID transactionId, final UUID organisationId, final String type, final int version) {
        return TransactionEntity.builder()
                .id(transactionId)
                .organisation_id(organisationId)
                .type(type)
                .version(version)
                .transaction_json("{}").build();
    }

    // Creates transaction equal to the one in payment.json and the first transaction in payments.json
    public static TransactionEntity createTransactionEntity() {
        try {
            return TransactionEntity.builder()
                    .id(TRANSACTION_ID_1)
                    .organisation_id(ORGANISATION_ID_1)
                    .type(TRANSACTION_TYPE)
                    .version(VERSION)
                    .transaction_json(MAPPER.writeValueAsString(createTransaction())).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Transaction createTransaction() {
        return createTransaction(UUID.fromString("4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"),
                UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"),
                "Payment", 0);
    }

    public static Transaction createTransaction(final UUID transactionId, final UUID organisationId, final String type, final int version) {
        Transaction transaction = Transaction.builder().id(transactionId)
                .organisation_id(organisationId)
                .type(type)
                .version(version).build();

        TransactionAttribute transactionAttribute = TransactionAttribute.builder()
                .amount(new BigDecimal("100.21"))
                .currency(GBP)
                .numeric_reference(1002001)
                .end_to_end_reference("Wil piano Jan")
                .payment_id("123456789012345678")
                .payment_purpose("Paying for goods/services")
                .payment_scheme("FPS")
                .payment_type("Credit")
                .processing_date(LocalDate.parse("2017-01-18", DateTimeFormatter.ISO_DATE))
                .scheme_payment_sub_type("InternetBanking")
                .scheme_payment_type("ImmediatePayment")
                .reference("Payment for Em's piano lessons")
                .build();

        TransactionParty sponsorParty = TransactionParty.builder()
                .account_number("56781234")
                .bank_id(123123)
                .bank_id_code("GBDSC").build();

        TransactionParty beneficiaryParty = TransactionParty.builder()
                .bank_id(403000)
                .bank_id_code("GBDSC")
                .account_name("W Owens")
                .account_number("31926819")
                .account_number_code("BBAN")
                .account_type(0)
                .address("1 The Beneficiary Localtown SE2")
                .name("Wilfred Jeremiah Owens")
                .build();

        TransactionParty debtorParty = TransactionParty.builder()
                .bank_id(203301)
                .bank_id_code("GBDSC")
                .account_name("EJ Brown Black")
                .account_number("GB29XABC10161234567801")
                .account_number_code("IBAN")
                .address("10 Debtor Crescent Sourcetown NE1")
                .name("Emelia Jane Brown")
                .build();

        transactionAttribute.setDebtor_party(debtorParty);
        transactionAttribute.setBeneficiary_party(beneficiaryParty);
        transactionAttribute.setSponsor_party(sponsorParty);

        Fx fx = Fx.builder()
                .contract_reference("FX123")
                .exchange_rate(new BigDecimal("2.00000").setScale(5))
                .original_amount(new BigDecimal("200.42"))
                .original_currency(USD)
                .build();

        transactionAttribute.setFx(fx);

        List<MonetaryAmount> senderCharges = asList(Money.of(5.00, GBP), Money.of(10.00, USD));

        ChargesInformation chargesInformation = ChargesInformation.builder()
                .bearer_code("SHAR")
                .sender_charges(senderCharges)
                .receiver_charges_amount(new BigDecimal("1.00"))
                .receiver_charges_currency(USD)
                .build();

        transactionAttribute.setCharges_information(chargesInformation);

        transaction.setTransactionAttribute(transactionAttribute);
        return transaction;
    }

    public static int count(final Session session, final String query) {
        return new Long(session.execute(query).one().getLong(0)).intValue();
    }

    public static int countTransactions(final Session session) {
        return count(session, "select count(id) from transaction");
    }

}
