CREATE TABLE outbox_message (
                                id UUID PRIMARY KEY,
                                aggregate_id VARCHAR(255) NOT NULL,
                                event_type VARCHAR(64) NOT NULL,
                                topic VARCHAR(255) NOT NULL,
                                payload JSONB NOT NULL,
                                created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                published_at TIMESTAMPTZ
);

CREATE INDEX idx_outbox_message_unpublished
    ON outbox_message (created_at)
    WHERE published_at IS NULL;