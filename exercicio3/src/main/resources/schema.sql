CREATE TABLE IF NOT EXISTS produto (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    quantidade INT NOT NULL,
    data_fabricacao DATE
);
