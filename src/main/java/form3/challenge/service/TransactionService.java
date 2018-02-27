package form3.challenge.service;

import form3.challenge.controller.dto.Transaction;
import form3.challenge.controller.dto.Transactions;
import form3.challenge.repository.TransactionRepository;
import form3.challenge.repository.domain.TransactionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    @Autowired
    private final TransactionRepository repository;

    public Optional<Transaction> getTransaction(UUID key) {
        Optional<TransactionEntity> transaction = repository.get(key);
        return transaction.isPresent() ? Optional.of(TransactionAdapter.of(transaction.get())) : Optional.empty();
    }

    // TODO limit the number of transactions returned.
    // Normally the date or some other limiting condition should be used
    public Transactions listTransactions() {
        List<TransactionEntity> transactionEntities = repository.getAll();
        List<Transaction> transactions = transactionEntities.stream().map(TransactionAdapter::of).collect(Collectors.toList());
        return new Transactions(transactions);
    }

    public void updateTransaction(Transaction transaction) {
        repository.save(TransactionAdapter.of(transaction));
    }

    public void saveTransaction(Transaction transaction) {
        // Cassandra does "upsert" up update, so insert and create are the same operations,
        // This method here is just for convenience
        updateTransaction(transaction);
    }

    public void saveTransactions(Transactions transactions) {
        repository.save(transactions.getData().stream().map(TransactionAdapter::of).collect(Collectors.toSet()));
    }

    public void deleteTransaction(UUID key) {
        repository.delete(key);
    }

    public void deleteTransactions(Set<UUID> keys) {
        repository.delete(keys);
    }

    // TODO Here we can implement all sorts of business rules on what the transaction can and cannot be
    public void validate(Transaction transaction) {
        checkNotNull(transaction);
        checkNotNull(transaction.getId());
    }

    // TODO Here we can implement all sorts of business rules on what the transactions object can and cannot be
    public void validate(Transactions transactions) {
        checkNotNull(transactions);
        checkState(transactions.getData() != null && ! transactions.getData().isEmpty());
    }
}
