package form3.challenge.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAttribute {
    private TransactionParty debtor_party;
    private TransactionParty beneficiary_party;
    private TransactionParty sponsor_party;
    private ChargesInformation charges_information;
    private Fx fx;

    private BigDecimal amount;
    private CurrencyUnit currency;
    private String end_to_end_reference;
    private long numeric_reference;
    private String payment_id;
    private String payment_purpose;
    private String payment_scheme;
    private String payment_type;
    private LocalDate processing_date; // Probably should be ZonedDateTime, but it's not evident from the data
    private String reference;
    private String scheme_payment_sub_type;
    private String scheme_payment_type;
}
