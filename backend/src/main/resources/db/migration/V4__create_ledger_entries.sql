CREATE TABLE ledger_entries (
    id BIGSERIAL PRIMARY KEY,

    farmer_id BIGINT NOT NULL,

    transaction_date DATE NOT NULL,

    entry_type VARCHAR(20) NOT NULL,

    transaction_type VARCHAR(30) NOT NULL,

    reference_id BIGINT,

    amount NUMERIC(14, 2) NOT NULL,

    description VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(id),

    CONSTRAINT chk_ledger_amount_positive
        CHECK (amount > 0),

    CONSTRAINT chk_ledger_entry_type
        CHECK (entry_type IN ('CREDIT', 'DEBIT')),

    CONSTRAINT chk_ledger_transaction_type
        CHECK (
            transaction_type IN (
                'MILK_ENTRY',
                'CASH_ADVANCE',
                'FEED_SALE',
                'PAYMENT',
                'ADJUSTMENT'
            )
        ),

    CONSTRAINT uq_ledger_reference
        UNIQUE (transaction_type, reference_id)
);

CREATE INDEX idx_ledger_farmer_date
    ON ledger_entries(farmer_id, transaction_date);

CREATE INDEX idx_ledger_reference
    ON ledger_entries(transaction_type, reference_id);