package form3.challenge.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.embedded.CassandraEmbeddedServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static info.archinnov.achilles.embedded.CassandraEmbeddedConfigParameters.CLUSTER_NAME;

@Configuration
@EnableConfigurationProperties(CassandraProperties.class)
public class TestConfiguration {

    CassandraProperties cassandraProperties;

    @Autowired
    public TestConfiguration(final CassandraProperties cassandraProperties) {
        this.cassandraProperties = cassandraProperties;
    }

    @Bean
    @Primary
    public Session session(final Cluster cluster) {
        if (cassandraProperties.getKeyspaceName() != null) {
            return cluster.connect(cassandraProperties.getKeyspaceName());
        }
        return cluster.connect();
    }


    @Bean(name = "sessionUnboundToKeyspace")
    public Session sessionUnboundToKeyspace(final Cluster cluster) {
        return cluster.connect();
    }


    @Bean
    @Primary
    public Cluster cluster() {

        final CassandraEmbeddedServerBuilder builder = CassandraEmbeddedServerBuilder
                .builder()
                .cleanDataFilesAtStartup(true)
                .withClusterName(CLUSTER_NAME)
                .withKeyspaceName(cassandraProperties.getKeyspaceName())
                .withScript("001-initial-create.cql");

        return builder.buildNativeCluster();
    }
}