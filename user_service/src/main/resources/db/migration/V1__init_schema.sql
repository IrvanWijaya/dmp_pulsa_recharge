CREATE TABLE IF NOT EXISTS roles (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL REFERENCES users(id),
    role_id VARCHAR NOT NULL REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);