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

-- żeby lepiej zoptymalizować zapytania wektorowe od razu po starcie --
ANALYZE vector_store;