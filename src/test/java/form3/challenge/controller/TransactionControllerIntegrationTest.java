package form3.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import form3.challenge.TestConstants;
import form3.challenge.controller.dto.Transaction;
import form3.challenge.repository.TransactionRepository;
import form3.challenge.service.TransactionAdapter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static form3.challenge.TestConstants.*;
import static form3.challenge.TestUtil.createTransaction;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// TODO could make use of controller's constants for URLs instead of having them hardcoded
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Autowired
    private MockMvc mockMvc;
    private MockHvcHelper mvcHelper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        List<Transaction> transactions = asList(
                TRANSACTION_0, TRANSACTION_1
        );
        transactions.stream().map(TransactionAdapter::of).forEach(i -> transactionRepository.save(i));
        mvcHelper = new MockHvcHelper(mockMvc);
    }

    @Test
    public void listTransactions() throws Exception {
        this.mockMvc.perform(get("/transactions"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(TestConstants.TRANSACTION_ID_1.toString()))
                .andExpect(jsonPath("$.data[0].version").value(TestConstants.VERSION))
                .andExpect(jsonPath("$.data[0].type").value(TestConstants.TRANSACTION_TYPE))
                .andExpect(jsonPath("$.data[0].organisation_id").value(TestConstants.ORGANISATION_ID_1.toString()))
                .andExpect(jsonPath("$.data[1].id").value(TestConstants.TRANSACTION_ID_0.toString()))
                .andExpect(jsonPath("$.data[1].version").value(TestConstants.VERSION))
                .andExpect(jsonPath("$.data[1].type").value(TestConstants.TRANSACTION_TYPE))
                .andExpect(jsonPath("$.data[0].organisation_id").value(TestConstants.ORGANISATION_ID_1.toString()))
                .andExpect(jsonPath("$.data[0].attributes.debtor_party").value(TRANSACTION_1.getTransactionAttribute().getDebtor_party()))
                .andExpect(jsonPath("$.data[0].attributes.beneficiary_party").value(TRANSACTION_1.getTransactionAttribute().getBeneficiary_party()))
                .andExpect(jsonPath("$.data[0].attributes.reference").value(TRANSACTION_1.getTransactionAttribute().getReference()))
                .andExpect(jsonPath("$.data[0].attributes.sponsor_party").value(TRANSACTION_1.getTransactionAttribute().getSponsor_party()))
                .andExpect(jsonPath("$.data[0].attributes.currency").value(TRANSACTION_1.getTransactionAttribute().getCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.data[0].attributes.fx.contract_reference").value(TRANSACTION_1.getTransactionAttribute().getFx().getContract_reference()))
                .andExpect(jsonPath("$.data[0].attributes.fx.exchange_rate").value(String.format("%.1f", TRANSACTION_1.getTransactionAttribute().getFx().getExchange_rate())))
                .andExpect(jsonPath("$.data[0].attributes.fx.original_amount").value(TRANSACTION_1.getTransactionAttribute().getFx().getOriginal_amount()))
                .andExpect(jsonPath("$.data[0].attributes.fx.original_currency").value(TRANSACTION_1.getTransactionAttribute().getFx().getOriginal_currency().getCurrencyCode()))
                .andExpect(jsonPath("$.data[0].attributes.end_to_end_reference").value(TRANSACTION_1.getTransactionAttribute().getEnd_to_end_reference()))
                .andExpect(jsonPath("$.data[0].attributes.amount").value(TRANSACTION_1.getTransactionAttribute().getAmount()))
                .andExpect(jsonPath("$.data[0].attributes.payment_id").value(TRANSACTION_1.getTransactionAttribute().getPayment_id()))
                .andExpect(jsonPath("$.data[0].attributes.numeric_reference").value(TRANSACTION_1.getTransactionAttribute().getNumeric_reference()))
                .andExpect(jsonPath("$.data[0].attributes.payment_purpose").value(TRANSACTION_1.getTransactionAttribute().getPayment_purpose()))
                .andExpect(jsonPath("$.data[0].attributes.payment_scheme").value(TRANSACTION_1.getTransactionAttribute().getPayment_scheme()))
                .andExpect(jsonPath("$.data[0].attributes.payment_type").value(TRANSACTION_1.getTransactionAttribute().getPayment_type()))
                .andExpect(jsonPath("$.data[0].attributes.processing_date").value(TRANSACTION_1.getTransactionAttribute().getProcessing_date().toString()))
                .andExpect(jsonPath("$.data[0].attributes.scheme_payment_sub_type").value(TRANSACTION_1.getTransactionAttribute().getScheme_payment_sub_type()))
                .andExpect(jsonPath("$.data[0].attributes.scheme_payment_type").value(TRANSACTION_1.getTransactionAttribute().getScheme_payment_type()))
                .andExpect(jsonPath("$.data[0].attributes.reference").value(TRANSACTION_1.getTransactionAttribute().getReference()))
                .andExpect(jsonPath("$.data[1].organisation_id").value(TestConstants.ORGANISATION_ID_0.toString()))
                .andExpect(jsonPath("$.data[1].attributes.debtor_party").value(TRANSACTION_0.getTransactionAttribute().getDebtor_party()))
                .andExpect(jsonPath("$.data[1].attributes.beneficiary_party").value(TRANSACTION_0.getTransactionAttribute().getBeneficiary_party()))
                .andExpect(jsonPath("$.data[1].attributes.reference").value(TRANSACTION_0.getTransactionAttribute().getReference()))
                .andExpect(jsonPath("$.data[1].attributes.sponsor_party").value(TRANSACTION_0.getTransactionAttribute().getSponsor_party()))
                .andExpect(jsonPath("$.data[1].attributes.currency").value(TRANSACTION_0.getTransactionAttribute().getCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.data[1].attributes.fx.contract_reference").value(TRANSACTION_0.getTransactionAttribute().getFx().getContract_reference()))
                .andExpect(jsonPath("$.data[1].attributes.fx.exchange_rate").value(String.format("%.1f", TRANSACTION_0.getTransactionAttribute().getFx().getExchange_rate())))
                .andExpect(jsonPath("$.data[1].attributes.fx.original_amount").value(TRANSACTION_0.getTransactionAttribute().getFx().getOriginal_amount()))
                .andExpect(jsonPath("$.data[1].attributes.fx.original_currency").value(TRANSACTION_0.getTransactionAttribute().getFx().getOriginal_currency().getCurrencyCode()))
                .andExpect(jsonPath("$.data[1].attributes.end_to_end_reference").value(TRANSACTION_0.getTransactionAttribute().getEnd_to_end_reference()))
                .andExpect(jsonPath("$.data[1].attributes.amount").value(TRANSACTION_0.getTransactionAttribute().getAmount()))
                .andExpect(jsonPath("$.data[1].attributes.payment_id").value(TRANSACTION_0.getTransactionAttribute().getPayment_id()))
                .andExpect(jsonPath("$.data[1].attributes.numeric_reference").value(TRANSACTION_0.getTransactionAttribute().getNumeric_reference()))
                .andExpect(jsonPath("$.data[1].attributes.payment_purpose").value(TRANSACTION_0.getTransactionAttribute().getPayment_purpose()))
                .andExpect(jsonPath("$.data[1].attributes.payment_scheme").value(TRANSACTION_0.getTransactionAttribute().getPayment_scheme()))
                .andExpect(jsonPath("$.data[1].attributes.payment_type").value(TRANSACTION_0.getTransactionAttribute().getPayment_type()))
                .andExpect(jsonPath("$.data[1].attributes.processing_date").value(TRANSACTION_0.getTransactionAttribute().getProcessing_date().toString()))
                .andExpect(jsonPath("$.data[1].attributes.scheme_payment_sub_type").value(TRANSACTION_0.getTransactionAttribute().getScheme_payment_sub_type()))
                .andExpect(jsonPath("$.data[1].attributes.scheme_payment_type").value(TRANSACTION_0.getTransactionAttribute().getScheme_payment_type()))
                .andExpect(jsonPath("$.data[1].attributes.reference").value(TRANSACTION_0.getTransactionAttribute().getReference()));
    }

    @Test
    public void getTransaction() throws Exception {
        mvcHelper.sync(MockMvcRequestBuilders.get("/transaction/{transactionId}", TRANSACTION_ID_0))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TRANSACTION_ID_0.toString()))
                .andExpect(jsonPath("$.version").value(VERSION))
                .andExpect(jsonPath("$.type").value(TRANSACTION_TYPE))
                .andExpect(jsonPath("$.organisation_id").value(ORGANISATION_ID_0.toString()))
                .andExpect(jsonPath("$.attributes.debtor_party").value(TRANSACTION_0.getTransactionAttribute().getDebtor_party()))
                .andExpect(jsonPath("$.attributes.beneficiary_party").value(TRANSACTION_0.getTransactionAttribute().getBeneficiary_party()))
                .andExpect(jsonPath("$.attributes.reference").value(TRANSACTION_0.getTransactionAttribute().getReference()))
                .andExpect(jsonPath("$.attributes.sponsor_party").value(TRANSACTION_0.getTransactionAttribute().getSponsor_party()))
                .andExpect(jsonPath("$.attributes.currency").value(TRANSACTION_0.getTransactionAttribute().getCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.attributes.fx.contract_reference").value(TRANSACTION_0.getTransactionAttribute().getFx().getContract_reference()))
                .andExpect(jsonPath("$.attributes.fx.exchange_rate").value(String.format("%.1f", TRANSACTION_0.getTransactionAttribute().getFx().getExchange_rate())))
                .andExpect(jsonPath("$.attributes.fx.original_amount").value(TRANSACTION_0.getTransactionAttribute().getFx().getOriginal_amount()))
                .andExpect(jsonPath("$.attributes.fx.original_currency").value(TRANSACTION_0.getTransactionAttribute().getFx().getOriginal_currency().getCurrencyCode()))
                .andExpect(jsonPath("$.attributes.end_to_end_reference").value(TRANSACTION_0.getTransactionAttribute().getEnd_to_end_reference()))
                .andExpect(jsonPath("$.attributes.amount").value(TRANSACTION_0.getTransactionAttribute().getAmount()))
                .andExpect(jsonPath("$.attributes.payment_id").value(TRANSACTION_0.getTransactionAttribute().getPayment_id()))
                .andExpect(jsonPath("$.attributes.numeric_reference").value(TRANSACTION_0.getTransactionAttribute().getNumeric_reference()))
                .andExpect(jsonPath("$.attributes.payment_purpose").value(TRANSACTION_0.getTransactionAttribute().getPayment_purpose()))
                .andExpect(jsonPath("$.attributes.payment_scheme").value(TRANSACTION_0.getTransactionAttribute().getPayment_scheme()))
                .andExpect(jsonPath("$.attributes.payment_type").value(TRANSACTION_0.getTransactionAttribute().getPayment_type()))
                .andExpect(jsonPath("$.attributes.processing_date").value(TRANSACTION_0.getTransactionAttribute().getProcessing_date().toString()))
                .andExpect(jsonPath("$.attributes.scheme_payment_sub_type").value(TRANSACTION_0.getTransactionAttribute().getScheme_payment_sub_type()))
                .andExpect(jsonPath("$.attributes.scheme_payment_type").value(TRANSACTION_0.getTransactionAttribute().getScheme_payment_type()))
                .andExpect(jsonPath("$.attributes.reference").value(TRANSACTION_0.getTransactionAttribute().getReference()));
    }

    @Test
    public void updateTransaction() throws Exception {
        Transaction transaction_0_copy = createTransaction(TestConstants.TRANSACTION_ID_0, TestConstants.ORGANISATION_ID_0, TestConstants.TRANSACTION_TYPE, TestConstants.VERSION);
        transaction_0_copy.setOrganisation_id(TRANSACTION_ID_2);
        transaction_0_copy.setVersion(1);
        transaction_0_copy.setType("Transfer");
        mvcHelper.syncPut("/transaction", mapper.writeValueAsString(transaction_0_copy))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void saveTransaction() throws Exception {
        Transaction transaction = createTransaction(TRANSACTION_ID_0, ORGANISATION_ID_0, TRANSACTION_TYPE, VERSION);
        String payload = mapper.writeValueAsString(transaction);
        mvcHelper.sync(MockMvcRequestBuilders.post("/transaction"), payload)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void saveTransactions() throws Exception {
        String payload = mapper.writeValueAsString(TRANSACTIONS);
        mvcHelper.sync(MockMvcRequestBuilders.post("/transactions"), payload)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteTransaction() throws Exception {
        mvcHelper.sync(MockMvcRequestBuilders.delete("/transaction/{TransactionId}", TRANSACTION_ID_0))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTransactions() throws Exception {
        mvcHelper.sync(MockMvcRequestBuilders.delete("/transactions/{TransactionId},{TransactionId}", TRANSACTION_ID_0, TRANSACTION_ID_1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}