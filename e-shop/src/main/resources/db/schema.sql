DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS vector_store CASCADE;

-- Włącz rozszerzenia potrzebne do vector_store
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;

-- Tabela vector_store
CREATE TABLE IF NOT EXISTS vector_store (
    id TEXT PRIMARY KEY,       -- id musi być TEXT
    content TEXT,
    metadata JSONB,
    embedding VECTOR(1024)
);

-- HNSW index dla szybkiego wyszukiwania
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx
    ON vector_store USING HNSW (embedding vector_cosine_ops);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto"; -- potrzebne do gen_random_uuid()
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    description TEXT,
    features TEXT,
    price NUMERIC(10, 2) NOT NULL,
    image_path VARCHAR(500),
    stock INT NOT NULL CHECK (stock >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    category_id BIGINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    user_id BIGINT,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    order_id BIGINT,
    product_id UUID,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id)
);

