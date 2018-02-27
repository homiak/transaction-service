package form3.challenge.repository;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Select;
import form3.challenge.repository.domain.TransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static form3.challenge.repository.domain.TransactionEntity.ID_COLUMN;


// TODO emit timers from each operation to get an idea about repository health
@Slf4j
@Repository
public class TransactionRepository extends CassandraRepository<TransactionEntity> {

    @Autowired
    public TransactionRepository(final Session session) {
        super(session, TransactionEntity.class);
    }

    public Optional<TransactionEntity> get(final UUID transactionId) {
        return selectOne(eq(ID_COLUMN, transactionId));
    }

    public List<TransactionEntity> getAll() {
        //We're asking Cassandra for all results over all nodes. Depending on the dataset size this might not be a good idea.
        Select select = createSelect().allowFiltering();
        List<TransactionEntity> transactions = select(select);
        log.debug("Got {} transactions", transactions.size());
        return transactions;
    }

    public void save(final TransactionEntity transaction) {
        super.save(transaction);
        log.debug("Saved transaction {}", transaction);
    }

    public void save(final Set<TransactionEntity> transactions) {
        super.save(Lists.newArrayList(transactions));
    }

    public void delete(final UUID transactionId) {
        final Delete delete = createDelete(eq(ID_COLUMN, transactionId));
        executeDelete(delete);
        log.debug("Deleted transaction {} ", transactionId);
    }

    public void delete(final Set<UUID> transactionIds) {
        transactionIds.stream().forEach(id -> {
            delete(id);
        });
    }

}
