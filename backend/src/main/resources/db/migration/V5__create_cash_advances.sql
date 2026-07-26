CREATE TABLE cash_advances (
    id BIGSERIAL PRIMARY KEY,

    farmer_id BIGINT NOT NULL,

    advance_date DATE NOT NULL,

    amount NUMERIC(12, 2) NOT NULL,

    remarks VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cash_advance_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(id),

    CONSTRAINT chk_cash_advance_amount_positive
        CHECK (amount > 0)
);

CREATE INDEX idx_cash_advances_farmer_id
    ON cash_advances(farmer_id);

CREATE INDEX idx_cash_advances_advance_date
    ON cash_advances(advance_date);