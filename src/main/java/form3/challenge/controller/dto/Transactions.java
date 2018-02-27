package form3.challenge.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {
    private List<Transaction> data;

    @ApiModelProperty(value = "HATEOAS links")
    @JsonProperty("links")
    private LinksDTO links;

    public Transactions(List<Transaction> data) {
        this.data = data;
    }

    @Data
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class LinksDTO {
        @ApiModelProperty(value = "Self link")
        @JsonProperty("self")
        private Link self;
    }
}
