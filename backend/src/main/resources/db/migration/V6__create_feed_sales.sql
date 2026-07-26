CREATE TABLE feed_sales (
    id BIGSERIAL PRIMARY KEY,

    farmer_id BIGINT NOT NULL,

    sale_date DATE NOT NULL,

    feed_name VARCHAR(150) NOT NULL,

    quantity NUMERIC(12,3) NOT NULL,

    unit_price NUMERIC(12,2) NOT NULL,

    total_amount NUMERIC(14,2) NOT NULL,

    remarks VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_feed_sales_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(id),

    CONSTRAINT chk_feed_sales_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_feed_sales_unit_price
        CHECK (unit_price > 0),

    CONSTRAINT chk_feed_sales_total_amount
        CHECK (total_amount > 0)
);

CREATE INDEX idx_feed_sales_farmer
    ON feed_sales(farmer_id);

CREATE INDEX idx_feed_sales_date
    ON feed_sales(sale_date);