
package form3.challenge.repository;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.datastax.driver.mapping.Mapper.Option.consistencyLevel;

/*
 This might seems as under-tested, I copied it from another project of myne where it was perfectly tested.
 Just for the sake of saving time did not move the tests.
 TODO test
 */
@Slf4j
public class CassandraRepository<T> {
    static final String MY_POD_NAMESPACE_ENV_VARIABLE = "MY_POD_NAMESPACE";
    static final String MY_POD_NAMESPACE_DEFAULT_NAMESPACE = "default";

    protected final Session session;
    protected final Mapper<T> mapper;
    protected final String table;
    protected final String keyspace;

    protected final ConsistencyLevel writeConsistency;
    protected final ConsistencyLevel readConsistency;

    protected final Class<T> clazz;

    protected CassandraRepository(final Session session, final Class<T> clazz) {
        this(session, clazz, ConsistencyLevel.LOCAL_QUORUM, ConsistencyLevel.EACH_QUORUM, null);
    }

    protected CassandraRepository(
            final Session session,
            final Class<T> clazz,
            final ConsistencyLevel readConsistency,
            final ConsistencyLevel writeConsistency,
            final String keyspaceOverride) {

        if (clazz.getAnnotation(Table.class) == null) {
            throw new IllegalArgumentException(clazz.getName() + " does not have the " + Table.class.getName() + " annotation.");
        }

        if (!StringUtils.isEmpty(clazz.getAnnotation(Table.class).keyspace())) {
            throw new IllegalArgumentException("@Table.keyspace is not supported anymore since we need to be able to dynamically set the" +
                    " keyspace. See Changelog lovebird commons for instructions");
        }

        this.session = session;
        this.clazz = clazz;
        this.readConsistency = readConsistency;
        this.writeConsistency = writeConsistency;
        this.table = clazz.getAnnotation(Table.class).name();

        if (!StringUtils.isEmpty(keyspaceOverride)) {
            keyspace = keyspaceOverride;
            mapper = new MappingManager(session).mapper(clazz, keyspaceOverride);
        } else {
            if (StringUtils.isEmpty(session.getLoggedKeyspace())) {
                throw new IllegalArgumentException("cannot resolve the keyspace for " + this.getClass().getName() +
                        " This should be configured on the session.");
            }
            keyspace = session.getLoggedKeyspace();
            mapper = new MappingManager(session).mapper(clazz);
        }

        if (readConsistency != null) {
            mapper.setDefaultGetOptions(consistencyLevel(readConsistency));
        }
        if (writeConsistency != null) {
            mapper.setDefaultDeleteOptions(consistencyLevel(writeConsistency));
            mapper.setDefaultSaveOptions(consistencyLevel(writeConsistency));
        }

        checkIsKeyspaceValid();
    }

    private void checkIsKeyspaceValid() {
        String podNamespace = System.getenv(MY_POD_NAMESPACE_ENV_VARIABLE);

        if (StringUtils.isEmpty(podNamespace)) {
            return;
        }
        if (podNamespace.trim().equals(MY_POD_NAMESPACE_DEFAULT_NAMESPACE)) {
            return;
        }

        if (!keyspace.startsWith(podNamespace)) {
            throw new IllegalArgumentException("Unable to create CassandraRepository. Keyspace should be prefixed with " + podNamespace);
        }

    }

    protected void save(final T entity, Mapper.Option... options) {
        try {
            mapper.save(entity, options);
            String auditMessageSuccess = String.format("Saved %s", table);
            log.info(auditMessageSuccess, entity);
        } catch (Throwable e) {
            String auditErrorMessage = String.format("Error while saving %s", table);
            log.error(auditErrorMessage, entity, e);
            throw e;
        }
    }

    protected void save(final T entity) {
        this.save(entity, new Mapper.Option[0]);
    }

    protected void save(final List<T> entities) {
        entities.forEach(this::save);
    }

    protected ResultSet executeDelete(final Delete delete) {
        try {
            ResultSet resultSet = session.execute(withConsistencyLevel(delete, writeConsistency));
            String auditMessageSuccess = String.format("Delete query executed : %s ", delete.toString());
            log.info(auditMessageSuccess);
            return resultSet;
        } catch (Throwable e) {
            String auditErrorMessage = String.format("Error while executing delete query : %s", table);
            log.error(auditErrorMessage, null, e);
            throw e;
        }
    }

    /**
     * Mind that this method is keyspace unaware, so you will have to set your keyspace when building the statement.
     * Only use this method when the select(Clause) method is insufficient.
     *
     * @param select The statement where the result will be mapped in a List of T.
     * @return The mapped objects of T as a result of the select statement.
     */
    protected List<T> select(final Statement select) {
        Result<T> result = mapper.map(session.execute(withConsistencyLevel(select, readConsistency)));

        return result.all();
    }

    protected Optional<T> selectOne(final Clause clause) {
        ResultSet resultSet = session.execute(withConsistencyLevel(createSelect(clause), readConsistency));
        Result<T> result = mapper.map(resultSet);

        return Optional.ofNullable(result.one());
    }

    protected Select createSelect() {
        return withConsistencyLevel(QueryBuilder.select().from(keyspace, table), readConsistency);
    }

    protected Select createSelect(final Clause clause) {
        Select select = createSelect();
        select.where(clause);

        return select;
    }

    protected Delete createDelete() {
        return withConsistencyLevel(QueryBuilder.delete().from(keyspace, table), writeConsistency);
    }

    protected Delete createDelete(final Clause clause) {
        Delete delete = createDelete();

        delete.where(clause);

        return delete;
    }

    private <T extends Statement> T withConsistencyLevel(final T statement, final ConsistencyLevel level) {
        if (level != null && statement.getConsistencyLevel() == null) {
            statement.setConsistencyLevel(level);
        }

        return statement;
    }
}
