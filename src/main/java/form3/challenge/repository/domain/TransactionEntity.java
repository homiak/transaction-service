package form3.challenge.repository.domain;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.*;

import java.util.UUID;

import static form3.challenge.repository.domain.TransactionEntity.TABLE_NAME;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = TABLE_NAME)
public class TransactionEntity {
    public static final String TABLE_NAME = "transaction";

    public static final String ID_COLUMN = "id";
    public static final String TYPE_COLUMN = "type";
    public static final String VERSION_COLUMN = "version";
    public static final String ORGANISATION_ID_COLUMN = "organisation_id";
    public static final String TRANSACTION_JSON_COLUMN = "transaction_json";

    @PartitionKey
    @Column(name = ID_COLUMN)
    private UUID id;

    @Column(name = TYPE_COLUMN)
    private String type;

    @Column(name = VERSION_COLUMN)
    private int version;

    @Column(name = ORGANISATION_ID_COLUMN)
    private UUID organisation_id;

    @Column(name = TRANSACTION_JSON_COLUMN)
    private String transaction_json;

}


