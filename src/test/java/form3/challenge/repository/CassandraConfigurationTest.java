package form3.challenge.repository;

import com.datastax.driver.core.ConsistencyLevel;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;

import static org.junit.Assert.*;

public class CassandraConfigurationTest {
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testClusterConsistencyLevel() {
        final CassandraProperties properties = new CassandraProperties();
        properties.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);

        expectedException.expect(UnsupportedOperationException.class);

        new CassandraConfiguration(properties);
    }

}