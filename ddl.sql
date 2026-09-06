-- Script DDL - Projeto Tio Patinhas
-- Integrante: Maria
-- Objetivo: Criação das tabelas

CREATE TABLE Usuario(
    id NUMBER PRIMARY KEY,
    nome VARCHAR2(100),
    cpf VARCHAR2(11),
    telefone VARCHAR2(20),
    email VARCHAR2(100),
    senhaHash VARCHAR2(200),
    dataCriacao TIMESTAMP
);

CREATE TABLE Carteira (
    id NUMBER PRIMARY KEY,
    usuario_id NUMBER,
    valorTotalInvestido NUMBER(15,2),
    valorAtual NUMBER(15,2),
    lucroPrejuizo NUMBER(15,2),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE Criptomoeda (
    id NUMBER PRIMARY KEY,
    nome VARCHAR2(100),
    simbolo VARCHAR2(10),
    descricao VARCHAR2(200)
);

CREATE TABLE Ativo (
    id NUMBER PRIMARY KEY,
    carteira_id NUMBER,
    criptomoeda_id NUMBER,
    quantidade NUMBER(15,2),
    precoMedio NUMBER(15,2),
    valorInvestido NUMBER(15,2),
    valorAtual NUMBER(15,2),
    CONSTRAINT fk_carteira FOREIGN KEY (carteira_id) REFERENCES carteira (id),
    CONSTRAINT fk_criptomoeda FOREIGN KEY (criptomoeda_id) REFERENCES criptomoeda (id)
);

CREATE TABLE Transacao (
    id NUMBER PRIMARY KEY,
    carteira_id NUMBER,
    criptomoeda_id NUMBER,
    tipo VARCHAR2(20),
    quantidade NUMBER(15,2),
    precoUnitario NUMBER(15,2),
    valorTotal NUMBER(15,2),
    dataHora TIMESTAMP,
    CONSTRAINT fk_carteira_transacao FOREIGN KEY (carteira_id) REFERENCES carteira(id),
    CONSTRAINT fk_criptomoeda_transacao FOREIGN KEY (criptomoeda_id) REFERENCES criptomoeda (id)
);

CREATE TABLE Favorito (
    id NUMBER PRIMARY KEY,
    usuario_id NUMBER,
    criptomoeda_id NUMBER,
    dataAdicionado TIMESTAMP,
    CONSTRAINT fk_usuario_favorito FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_criptomoeda_favorito FOREIGN KEY (criptomoeda_id) REFERENCES criptomoeda (id)
);

CREATE TABLE Cotacao (
    id NUMBER PRIMARY KEY,
    criptomoeda_id NUMBER,
    precoAtual NUMBER(15,2),
    variacao24h NUMBER(10,2),
    variacao7d NUMBER(10,2),
    variacao30d NUMBER(10,2),
    dataConsulta TIMESTAMP,
    CONSTRAINT fk_criptomoeda_cotacao FOREIGN KEY (criptomoeda_id) REFERENCES criptomoeda (id)
);

CREATE TABLE Aporte (
    id NUMBER PRIMARY KEY,
    carteira_id NUMBER,
    valor NUMBER(15,2),
    dataHora TIMESTAMP,
    descricao VARCHAR2(200),
    CONSTRAINT fk_carteira_aporte FOREIGN KEY (carteira_id) REFERENCES carteira (id)
);

-- =========================================================
-- ALTER
-- Ajustes estruturais e restricoes adicionais nas tabelas.
-- =========================================================

ALTER TABLE Usuario
MODIFY (
    nome VARCHAR2(100) NOT NULL,
    cpf VARCHAR2(11) NOT NULL,
    email VARCHAR2(100) NOT NULL
);

ALTER TABLE Usuario
ADD CONSTRAINT uk_usuario_cpf UNIQUE (cpf);

ALTER TABLE Usuario
ADD CONSTRAINT uk_usuario_email UNIQUE (email);

ALTER TABLE Criptomoeda
MODIFY (
    nome VARCHAR2(100) NOT NULL,
    simbolo VARCHAR2(10) NOT NULL
);

ALTER TABLE Criptomoeda
ADD CONSTRAINT uk_criptomoeda_simbolo UNIQUE (simbolo);

ALTER TABLE Transacao
ADD CONSTRAINT ck_transacao_tipo CHECK (tipo IN ('COMPRA', 'VENDA'));

ALTER TABLE Ativo
ADD CONSTRAINT ck_ativo_quantidade CHECK (quantidade >= 0);

ALTER TABLE Aporte
ADD CONSTRAINT ck_aporte_valor CHECK (valor > 0);

ALTER TABLE Carteira
ADD dataAtualizacao TIMESTAMP;

-- =========================================================
-- DROP
-- Exemplo de exclusao de estrutura sem remover tabelas finais do projeto. A tabela abaixo e criada apenas para demonstrar o comando DROP TABLE.
-- =========================================================

CREATE TABLE LogAuditoriaTemp (
    id NUMBER PRIMARY KEY,
    descricao VARCHAR2(200),
    dataRegistro TIMESTAMP
);

DROP TABLE LogAuditoriaTemp;

