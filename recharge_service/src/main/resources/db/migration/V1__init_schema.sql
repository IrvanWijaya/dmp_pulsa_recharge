CREATE TABLE IF NOT EXISTS recharge_requests (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    operator VARCHAR NOT NULL,
    third_party_id VARCHAR,
    amount INT NOT NULL,
    request_at TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL,
    end_at TIMESTAMP
);