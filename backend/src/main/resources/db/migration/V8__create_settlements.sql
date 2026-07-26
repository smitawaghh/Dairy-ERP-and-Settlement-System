CREATE TABLE settlements (

    id BIGSERIAL PRIMARY KEY,

    farmer_id BIGINT NOT NULL,

    period_from DATE NOT NULL,

    period_to DATE NOT NULL,

    total_credits NUMERIC(14, 2) NOT NULL,

    total_debits NUMERIC(14, 2) NOT NULL,

    net_amount NUMERIC(14, 2) NOT NULL,

    status VARCHAR(30) NOT NULL,

    remarks VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_settlements_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(id),

    CONSTRAINT chk_settlement_period
        CHECK (period_to >= period_from),

    CONSTRAINT chk_settlement_credits
        CHECK (total_credits >= 0),

    CONSTRAINT chk_settlement_debits
        CHECK (total_debits >= 0)
);

CREATE INDEX idx_settlements_farmer_period
    ON settlements(
        farmer_id,
        period_from,
        period_to
    );