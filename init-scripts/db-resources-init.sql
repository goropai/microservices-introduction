CREATE DATABASE resources;
\c resources;
CREATE TABLE IF NOT EXISTS resources (
    id SERIAL PRIMARY KEY,
    data BYTEA NOT NULL
);