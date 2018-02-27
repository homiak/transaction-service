package form3.challenge.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionParty {
    private String account_name;
    private String account_number;
    private String account_number_code;
    private Integer account_type;
    private String address;
    private long bank_id;
    private String bank_id_code;
    private String name;
}
