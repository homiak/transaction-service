package form3.challenge.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fx {

    private String contract_reference;
    private BigDecimal exchange_rate;

    // It would be possible to represent the two fields below as MonetaryAmount
    // However, that would mean that we need to write a custom serializer/deserializer here
    // So this is a judgment call
    private BigDecimal original_amount;
    private CurrencyUnit original_currency;

}
