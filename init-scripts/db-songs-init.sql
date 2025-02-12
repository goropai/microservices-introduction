CREATE TABLE IF NOT EXISTS songs (
    id SERIAL PRIMARY KEY,
    artist VARCHAR(100),
    name VARCHAR(100),
    album VARCHAR(100),
    year VARCHAR(4),
    duration VARCHAR(100)
);