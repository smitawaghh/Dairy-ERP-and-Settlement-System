CREATE TABLE payments (

    id BIGSERIAL PRIMARY KEY,

    farmer_id BIGINT NOT NULL,

    payment_date DATE NOT NULL,

    amount NUMERIC(12, 2) NOT NULL,

    payment_mode VARCHAR(30) NOT NULL,

    reference_number VARCHAR(100),

    remarks VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(id),

    CONSTRAINT chk_payments_amount
        CHECK (amount > 0)
);

CREATE INDEX idx_payments_farmer_id
    ON payments(farmer_id);

CREATE INDEX idx_payments_payment_date
    ON payments(payment_date);