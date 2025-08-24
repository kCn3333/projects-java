-- ======================
-- Table: clients
-- ======================
-- Drop table if exists.
DROP TABLE IF EXISTS clients;

-- Create clients table
CREATE TABLE clients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(45),
    last_name VARCHAR(45),
    email VARCHAR(45)
);

-- ======================
-- Table: users and roles
-- ======================
-- Usuwamy stare tabele (najpierw role, bo mają FK do users)
DROP TABLE IF EXISTS app_authorities;
DROP TABLE IF EXISTS app_users;

-- Tabela użytkowników
CREATE TABLE app_users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Tabela ról (authority)
CREATE TABLE app_authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_app_authorities_user FOREIGN KEY(username) REFERENCES app_users(username)
);

-- Indeks unikalny, żeby nie powtarzać roli dla jednego usera
CREATE UNIQUE INDEX ix_app_auth_username_authority ON app_authorities (username, authority);
