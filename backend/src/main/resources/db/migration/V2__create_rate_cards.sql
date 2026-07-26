CREATE TABLE rate_cards (
    id BIGSERIAL PRIMARY KEY,

    milk_type VARCHAR(20) NOT NULL,

    fat_rate NUMERIC(10,4) NOT NULL,
    snf_rate NUMERIC(10,4) NOT NULL,

    effective_from DATE NOT NULL,
    effective_to DATE,

    remarks TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_rate_card_milk_type
        CHECK (milk_type IN ('COW', 'BUFFALO')),

    CONSTRAINT chk_rate_card_fat_rate
        CHECK (fat_rate >= 0),

    CONSTRAINT chk_rate_card_snf_rate
        CHECK (snf_rate >= 0),

    CONSTRAINT chk_rate_card_dates
        CHECK (
            effective_to IS NULL
            OR effective_to >= effective_from
        )
);

CREATE INDEX idx_rate_cards_milk_type
    ON rate_cards(milk_type);

CREATE INDEX idx_rate_cards_effective_period
    ON rate_cards(
        milk_type,
        effective_from,
        effective_to
    );