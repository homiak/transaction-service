package form3.challenge.controller;

import form3.challenge.controller.dto.Transaction;
import form3.challenge.controller.dto.Transactions;
import form3.challenge.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = TransactionController.CONTROLLER_PATH)
@RequiredArgsConstructor
public class TransactionController {
    public static final String CONTROLLER_PATH = "/";
    public static final String TRANSACTION_PATH_BARE = "/transaction";
    public static final String TRANSACTION_PATH = "/transaction/{transactionId}";
    public static final String TRANSACTIONS_PATH = "/transactions";
    public static final String TRANSACTIONS_IDS_PATH = "/transactions/{transactionIds}";

    private final TransactionService service;

    @GetMapping(value = TRANSACTIONS_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all transactions", response = Transactions.class)
    public ResponseEntity<Transactions> listTransactions() {
        Transactions transactions = service.listTransactions();
        log.info("Got {} transactions", transactions.getData().size());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(value = TRANSACTION_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Returns a single transaction", response = Transaction.class)
    public ResponseEntity<Transaction> getTransaction(@PathVariable("transactionId") final UUID transactionIds) {
        // Normally I would add header with request traceId to help with request tracing,
        // but I am going to skip this here for the sake of simplicity
        Optional<Transaction> transaction = service.getTransaction(transactionIds);
        if (transaction.isPresent()) {
            log.info("Got {} transaction for key {}", transaction.get(), transactionIds);
            return ResponseEntity.ok(transaction.get());

        } else {
            log.warn("No such transaction {}", transactionIds);
            return ResponseEntity.notFound().build();
        }
    }

    /* We are using Cassandra in the backend so update or create is the same. Thus calling this method has the same
     result as calling saveTransaction(Transaction transaction). This method is in the API to comply with REST specs.
    */
    @PutMapping(value = TRANSACTION_PATH_BARE)
    @ApiOperation(value = "Updates the transaction")
    public ResponseEntity<?> updateTransaction(@RequestBody final Transaction transaction) {
        log.info("Updating transaction {}", transaction.getId());
        service.validate(transaction);
        service.updateTransaction(transaction);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = TRANSACTIONS_PATH)
    @ApiOperation(value = "Creates/saves the transaction")
    public ResponseEntity<?> saveTransaction(@RequestBody final Transactions transactions) {
        log.info("Saving {} transactions", transactions.getData().size());
        service.validate(transactions);
        service.saveTransactions(transactions);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = TRANSACTION_PATH_BARE)
    @ApiOperation(value = "Creates/saves the transaction")
    public ResponseEntity<?> saveTransaction(@RequestBody final Transaction transaction) {
        log.info("Saving transaction {}", transaction.getId());
        service.validate(transaction);
        service.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = TRANSACTION_PATH)
    @ApiOperation(value = "Deletes a transaction")
    public ResponseEntity<?> deleteTransaction(@PathVariable("transactionId") final UUID transactionId) {
        log.info("Deleting transaction {}", transactionId);
        service.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = TRANSACTIONS_IDS_PATH)
    @ApiOperation(value = "Deletes transactions by id")
    public ResponseEntity<?> deleteTransactions(@PathVariable final Set<UUID> transactionIds) {
        log.info("Deleting {} transactions by id", transactionIds.size());
        service.deleteTransactions(transactionIds);
        return ResponseEntity.noContent().build();
    }
}
