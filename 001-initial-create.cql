CREATE KEYSPACE IF NOT EXISTS transactions WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '1'} AND durable_writes = true;

CREATE TABLE IF NOT EXISTS transactions.transaction (
    id UUID PRIMARY KEY,
    organisation_id UUID,
    type text,
    version int,
    transaction_json text
);