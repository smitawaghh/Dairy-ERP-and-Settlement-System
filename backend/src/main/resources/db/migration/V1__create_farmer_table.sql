CREATE TABLE farmers (

    id BIGSERIAL PRIMARY KEY,

    farmer_code VARCHAR(20) NOT NULL UNIQUE,

    full_name VARCHAR(100) NOT NULL,

    mobile VARCHAR(15) NOT NULL,

    village VARCHAR(100) NOT NULL,

    address TEXT,

    bank_account_number VARCHAR(30),

    ifsc VARCHAR(11),

    aadhaar VARCHAR(12),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    remarks TEXT,

    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_by BIGINT,

    updated_by BIGINT,

    updated_at TIMESTAMP

);

CREATE INDEX idx_farmer_code
ON farmers(farmer_code);

CREATE INDEX idx_farmer_name
ON farmers(full_name);

CREATE INDEX idx_farmer_mobile
ON farmers(mobile);