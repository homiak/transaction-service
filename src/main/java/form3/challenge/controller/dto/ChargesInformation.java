package form3.challenge.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargesInformation {

    private String bearer_code;
    private List<MonetaryAmount> sender_charges;

    // It would be possible to represent the two fields below as MonetaryAmount
    // However, that would mean that we need to write a custom serializer/deserializer here
    // So this is a judgment call
    private BigDecimal receiver_charges_amount;
    private CurrencyUnit receiver_charges_currency;

}
