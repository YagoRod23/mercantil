CREATE TABLE produto (
    id             BIGSERIAL PRIMARY KEY,
    nome           VARCHAR(255)   NOT NULL,
    codigo_barras  VARCHAR(50)    UNIQUE,
    preco          NUMERIC(10, 2) NOT NULL,
    ativo          BOOLEAN        NOT NULL DEFAULT TRUE
);
