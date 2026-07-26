CREATE TABLE milk_entries (

    id BIGSERIAL PRIMARY KEY,

    farmer_id BIGINT NOT NULL,

    collection_date DATE NOT NULL,

    collection_shift VARCHAR(20) NOT NULL,

    milk_type VARCHAR(20) NOT NULL,

    quantity NUMERIC(10,2) NOT NULL,

    fat NUMERIC(5,2) NOT NULL,

    snf NUMERIC(5,2) NOT NULL,

    fat_rate NUMERIC(10,4) NOT NULL,

    snf_rate NUMERIC(10,4) NOT NULL,

    rate_per_litre NUMERIC(12,4) NOT NULL,

    amount NUMERIC(14,2) NOT NULL,

    remarks VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_milk_entries_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(id),

    CONSTRAINT chk_milk_quantity_positive
        CHECK (quantity > 0),

    CONSTRAINT chk_milk_fat_range
        CHECK (fat >= 0 AND fat <= 15),

    CONSTRAINT chk_milk_snf_range
        CHECK (snf >= 0 AND snf <= 15),

    CONSTRAINT chk_collection_shift
        CHECK (collection_shift IN ('MORNING', 'EVENING')),

    CONSTRAINT chk_milk_type
        CHECK (milk_type IN ('COW', 'BUFFALO')),

    CONSTRAINT uk_milk_entry_farmer_date_shift_type
        UNIQUE (
            farmer_id,
            collection_date,
            collection_shift,
            milk_type
        )
);

CREATE INDEX idx_milk_entries_farmer
    ON milk_entries(farmer_id);

CREATE INDEX idx_milk_entries_collection_date
    ON milk_entries(collection_date);

CREATE INDEX idx_milk_entries_farmer_date
    ON milk_entries(farmer_id, collection_date);