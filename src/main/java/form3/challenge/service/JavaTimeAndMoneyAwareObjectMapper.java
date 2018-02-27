package form3.challenge.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.io.IOException;

/**
 * Wrapper around jackson ObjectMapper that is registered with a
 * JavaTimeModule.
 */
@Component
public class JavaTimeAndMoneyAwareObjectMapper {
    private ObjectMapper mapper;

    public JavaTimeAndMoneyAwareObjectMapper() {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new MoneyModule().withMoney());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    public <T> T readValue(String json, Class<T> valueType) throws IOException {
        return mapper.readValue(json, valueType);
    }
}

