package form3.challenge.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LoggingRetryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * {@link org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration} does not instantiate
 * the LoadBalancingPolicies or ReconnectionPolicies or RetryPolicies correctly since its trying to create an instance
 * from the NoArgs constructor which is private or does not exists.
 */
@Configuration
@ConditionalOnClass({Cluster.class})
@EnableConfigurationProperties(CassandraProperties.class)
public class CassandraConfiguration {

    @Value("${spring.data.cassandra.datacenter-name}")
    private String datacenterName;

    private final CassandraProperties properties;

    public CassandraConfiguration(CassandraProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Cluster cluster() {
        CassandraProperties properties = this.properties;
        Cluster.Builder builder = Cluster.builder()
                .withClusterName(properties.getClusterName())
                .withPort(properties.getPort());
        if (properties.getUsername() != null) {
            builder.withCredentials(properties.getUsername(), properties.getPassword());
        }
        if (properties.getCompression() != null) {
            builder.withCompression(properties.getCompression());
        }
        builder.withLoadBalancingPolicy(
                DCAwareRoundRobinPolicy.builder()
                        .withLocalDc(datacenterName).build());
        if (properties.getRetryPolicy() != null) {
            throw new UnsupportedOperationException("We always have a retry policy in place that cannot be over-ridden.");
        }
        builder.withSocketOptions(getSocketOptions());
        if (properties.isSsl()) {
            builder.withSSL();
        }
        builder.withQueryOptions(getQueryOptions());
        String points = properties.getContactPoints();
        builder.addContactPoints(StringUtils.commaDelimitedListToStringArray(points));
        return builder.build();
    }

    @Bean
    public Session session(Cluster cluster) {

        if (properties.getKeyspaceName() != null) {
            return cluster.connect(properties.getKeyspaceName());
        }
        return cluster.connect();
    }

    private QueryOptions getQueryOptions() {
        QueryOptions options = new QueryOptions();
        CassandraProperties properties = this.properties;
        if (properties.getConsistencyLevel() != null) {
            throw new UnsupportedOperationException("Please set your consistency level at either repository or query-level.");
        }
        if (properties.getSerialConsistencyLevel() != null) {
            options.setSerialConsistencyLevel(properties.getSerialConsistencyLevel());
        }
        options.setFetchSize(properties.getFetchSize());
        return options;
    }

    private SocketOptions getSocketOptions() {
        SocketOptions options = new SocketOptions();
        options.setConnectTimeoutMillis(this.properties.getConnectTimeoutMillis());
        options.setReadTimeoutMillis(this.properties.getReadTimeoutMillis());
        return options;
    }

}
