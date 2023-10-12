CREATE TABLE IF NOT EXISTS transaction(
    id VARCHAR(55) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(255),
    record_flag VARCHAR(55),
    account_code VARCHAR(55),
    note VARCHAR(255),
    amount NUMERIC,
    username VARCHAR(55),
    datetime TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT transaction_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS account_code(
    id VARCHAR(55) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(255),
    record_flag VARCHAR(55),
    name VARCHAR(255),
    username VARCHAR(55),
    CONSTRAINT account_code_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users(
    id VARCHAR(55) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(255),
    record_flag VARCHAR(55),
    username VARCHAR(55),
    first_name VARCHAR(55),
    last_name VARCHAR(55),
    email VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT user_pk PRIMARY KEY (id)
);
