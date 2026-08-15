CREATE TABLE app_users (

    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(100) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_app_users_username
        UNIQUE (username),

    CONSTRAINT chk_app_users_role
        CHECK (role IN ('ADMIN', 'OPERATOR'))
);

CREATE INDEX idx_app_users_username
    ON app_users(username);