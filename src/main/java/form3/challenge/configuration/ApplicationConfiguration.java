package form3.challenge.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.zalando.jackson.datatype.money.MoneyModule;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import java.math.BigDecimal;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@ComponentScan("form3")
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

    @Value("${info.appName}")
    private String applicationName;

    @Value("${info.build.version}")
    private String applicationVersion;

    @Value("${form3.thread.pool.size:10}")
    private int maxWebThreadPoolSize;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Swagger beans.
     */
    @Bean
    public Docket apiDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("transactions")
                .apiInfo(apiInfo())
                // Not ideal because it leads to swagger enable to resolve these types when they come back on request replay
                // but this is better then having a skewed data model (more complicated than it actually is)
                .directModelSubstitute(MonetaryAmount.class, BigDecimal.class)
                .directModelSubstitute(CurrencyUnit.class, String.class)
                .select()
                .paths(regex("/transaction.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(String.format("Documentation for %s", applicationName))
                .description("Swagger API documentation")
                .version(applicationVersion)
                .build();
    }

    @PostConstruct
    public void configureObjectMapper() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new MoneyModule().withMoney());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    protected WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
                configurer.setTaskExecutor(asyncExecutor());
            }
        };
    }

    private ThreadPoolTaskExecutor asyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(maxWebThreadPoolSize);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("MVC-Executor-");
        executor.initialize();
        return executor;
    }
}
