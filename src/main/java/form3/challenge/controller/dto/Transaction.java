package form3.challenge.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    /* You'll find that @ApiModelProperty annotations mostly in this class because of the
    time constraints and the fact that I am not a domain expert :-)
    TODO annotate the rest of the model */
    @ApiModelProperty(value = "Transaction key", required = true, example = "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43")
    UUID id;

    @ApiModelProperty(value = "Transaction type", example = "Payment")
    String type; // TODO make it a enum

    int version;

    // I'd normally name it organisationId. However, that would mean I have to use
    // @JsonProperty("organisation_id") to give it the name that matches your json.
    // Not sure if it is worth it
    @ApiModelProperty(value = "Organisation Id", example = "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb")
    UUID organisation_id;

    // Class names with underscores are weird, so I am going to make an extra effort here :-)
    @JsonProperty("attributes")
    @ApiModelProperty(value = "Transaction details")
    TransactionAttribute transactionAttribute;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("id", id)
                .add("version", version)
                .add("organisation_id", organisation_id)
                .toString();
    }
}
