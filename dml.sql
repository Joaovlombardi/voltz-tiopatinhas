-- Script DML - Projeto Voltz / Tio Patinhas
-- Objetivo: popular as tabelas criadas no ddl.sql e demonstrar comandos DML

-- =========================================================
-- DELETE
-- Limpeza dos dados respeitando a ordem das chaves estrangeiras.
-- =========================================================

DELETE FROM Cotacao;
DELETE FROM Favorito;
DELETE FROM Transacao;
DELETE FROM Aporte;
DELETE FROM Ativo;
DELETE FROM Carteira;
DELETE FROM Criptomoeda;
DELETE FROM Usuario;

-- =========================================================
-- INSERT
-- Carga inicial de usuarios, carteiras, criptomoedas, ativos, transacoes, favoritos, cotacoes e aportes.
-- =========================================================

INSERT INTO Usuario (
    id, nome, cpf, telefone, email, senhaHash, dataCriacao
) VALUES (
    1,
    'Miguel Monteiro',
    '11111111111',
    '(11) 99999-8888',
    'miguel@email.com',
    'hash_senha_miguel',
    TO_TIMESTAMP('2026-08-19 08:15:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Usuario (
    id, nome, cpf, telefone, email, senhaHash, dataCriacao
) VALUES (
    2,
    'Joao Vitor',
    '12345678900',
    '(11) 99999-9999',
    'joao@email.com',
    'hash_senha_joao',
    TO_TIMESTAMP('2026-08-19 09:00:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Usuario (
    id, nome, cpf, telefone, email, senhaHash, dataCriacao
) VALUES (
    3,
    'Maria Eduarda',
    '98765432100',
    '(11) 98888-7777',
    'maria@email.com',
    'hash_senha_maria',
    TO_TIMESTAMP('2026-08-19 09:15:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Carteira (
    id, usuario_id, valorTotalInvestido, valorAtual, lucroPrejuizo
) VALUES (
    1, 1, 0, 0, 0
);

INSERT INTO Carteira (
    id, usuario_id, valorTotalInvestido, valorAtual, lucroPrejuizo
) VALUES (
    2, 2, 0, 0, 0
);

INSERT INTO Criptomoeda (
    id, nome, simbolo, descricao
) VALUES (
    1,
    'Bitcoin',
    'BTC',
    'Principal criptomoeda do mercado'
);

INSERT INTO Criptomoeda (
    id, nome, simbolo, descricao
) VALUES (
    2,
    'Ethereum',
    'ETH',
    'Plataforma descentralizada para contratos inteligentes'
);

INSERT INTO Criptomoeda (
    id, nome, simbolo, descricao
) VALUES (
    3,
    'Solana',
    'SOL',
    'Blockchain de alta performance'
);

INSERT INTO Cotacao (
    id, criptomoeda_id, precoAtual, variacao24h, variacao7d, variacao30d, dataConsulta
) VALUES (
    1,
    1,
    320000.00,
    1.80,
    4.25,
    12.40,
    TO_TIMESTAMP('2026-08-19 10:00:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Cotacao (
    id, criptomoeda_id, precoAtual, variacao24h, variacao7d, variacao30d, dataConsulta
) VALUES (
    2,
    2,
    22000.00,
    -0.75,
    3.10,
    8.90,
    TO_TIMESTAMP('2026-08-19 10:00:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Cotacao (
    id, criptomoeda_id, precoAtual, variacao24h, variacao7d, variacao30d, dataConsulta
) VALUES (
    3,
    3,
    850.00,
    2.30,
    6.80,
    15.50,
    TO_TIMESTAMP('2026-08-19 10:00:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Ativo (
    id, carteira_id, criptomoeda_id, quantidade, precoMedio, valorInvestido, valorAtual
) VALUES (
    1,
    1,
    1,
    0.50,
    300000.00,
    150000.00,
    160000.00
);

INSERT INTO Ativo (
    id, carteira_id, criptomoeda_id, quantidade, precoMedio, valorInvestido, valorAtual
) VALUES (
    2,
    1,
    2,
    10.00,
    20000.00,
    200000.00,
    220000.00
);

INSERT INTO Ativo (
    id, carteira_id, criptomoeda_id, quantidade, precoMedio, valorInvestido, valorAtual
) VALUES (
    3,
    2,
    3,
    25.00,
    780.00,
    19500.00,
    21250.00
);

INSERT INTO Transacao (
    id, carteira_id, criptomoeda_id, tipo, quantidade, precoUnitario, valorTotal, dataHora
) VALUES (
    1,
    1,
    1,
    'COMPRA',
    0.50,
    300000.00,
    150000.00,
    TO_TIMESTAMP('2026-08-19 10:30:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Transacao (
    id, carteira_id, criptomoeda_id, tipo, quantidade, precoUnitario, valorTotal, dataHora
) VALUES (
    2,
    1,
    2,
    'COMPRA',
    10.00,
    20000.00,
    200000.00,
    TO_TIMESTAMP('2026-08-19 10:45:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Transacao (
    id, carteira_id, criptomoeda_id, tipo, quantidade, precoUnitario, valorTotal, dataHora
) VALUES (
    3,
    2,
    3,
    'COMPRA',
    25.00,
    780.00,
    19500.00,
    TO_TIMESTAMP('2026-08-19 11:00:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Aporte (
    id, carteira_id, valor, dataHora, descricao
) VALUES (
    1,
    1,
    150000.00,
    TO_TIMESTAMP('2026-08-19 10:20:00', 'YYYY-MM-DD HH24:MI:SS'),
    'Aporte inicial para compra de Bitcoin'
);

INSERT INTO Aporte (
    id, carteira_id, valor, dataHora, descricao
) VALUES (
    2,
    1,
    200000.00,
    TO_TIMESTAMP('2026-08-19 10:35:00', 'YYYY-MM-DD HH24:MI:SS'),
    'Aporte adicional para compra de Ethereum'
);

INSERT INTO Aporte (
    id, carteira_id, valor, dataHora, descricao
) VALUES (
    3,
    2,
    19500.00,
    TO_TIMESTAMP('2026-08-19 10:55:00', 'YYYY-MM-DD HH24:MI:SS'),
    'Aporte inicial para compra de Solana'
);

INSERT INTO Favorito (
    id, usuario_id, criptomoeda_id, dataAdicionado
) VALUES (
    1,
    1,
    1,
    TO_TIMESTAMP('2026-08-19 11:10:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Favorito (
    id, usuario_id, criptomoeda_id, dataAdicionado
) VALUES (
    2,
    1,
    2,
    TO_TIMESTAMP('2026-08-19 11:12:00', 'YYYY-MM-DD HH24:MI:SS')
);

INSERT INTO Favorito (
    id, usuario_id, criptomoeda_id, dataAdicionado
) VALUES (
    3,
    2,
    3,
    TO_TIMESTAMP('2026-08-19 11:15:00', 'YYYY-MM-DD HH24:MI:SS')
);

-- =========================================================
-- UPDATE
-- Atualizacao de dados cadastrais e consolidacao das carteiras.
-- =========================================================

UPDATE Usuario
SET telefone = '(21) 97777-6666'
WHERE id = 1;

UPDATE Carteira c
SET
    valorTotalInvestido = (
        SELECT NVL(SUM(a.valorInvestido), 0)
        FROM Ativo a
        WHERE a.carteira_id = c.id
    ),
    valorAtual = (
        SELECT NVL(SUM(a.valorAtual), 0)
        FROM Ativo a
        WHERE a.carteira_id = c.id
    ),
    lucroPrejuizo = (
        SELECT NVL(SUM(a.valorAtual - a.valorInvestido), 0)
        FROM Ativo a
        WHERE a.carteira_id = c.id
    );

UPDATE Cotacao
SET precoAtual = 321500.00,
    variacao24h = 2.10,
    dataConsulta = TO_TIMESTAMP('2026-08-19 12:00:00', 'YYYY-MM-DD HH24:MI:SS')
WHERE criptomoeda_id = 1;

-- =========================================================
-- SELECT
-- Consultas para validar a carga de dados.
-- =========================================================

SELECT * FROM Usuario;

SELECT * FROM Criptomoeda;

SELECT
    u.nome AS usuario,
    c.id AS carteira_id,
    c.valorTotalInvestido,
    c.valorAtual,
    c.lucroPrejuizo
FROM Usuario u
JOIN Carteira c ON c.usuario_id = u.id
ORDER BY u.id, c.id;

SELECT
    u.nome AS usuario,
    cr.nome AS criptomoeda,
    cr.simbolo,
    a.quantidade,
    a.precoMedio,
    a.valorInvestido,
    a.valorAtual
FROM Ativo a
JOIN Carteira c ON c.id = a.carteira_id
JOIN Usuario u ON u.id = c.usuario_id
JOIN Criptomoeda cr ON cr.id = a.criptomoeda_id
ORDER BY u.id, cr.simbolo;

SELECT
    c.id AS carteira_id,
    cr.simbolo,
    t.tipo,
    t.quantidade,
    t.precoUnitario,
    t.valorTotal,
    t.dataHora
FROM Transacao t
JOIN Carteira c ON c.id = t.carteira_id
JOIN Criptomoeda cr ON cr.id = t.criptomoeda_id
ORDER BY t.dataHora;

SELECT
    u.nome AS usuario,
    cr.nome AS criptomoeda_favorita,
    f.dataAdicionado
FROM Favorito f
JOIN Usuario u ON u.id = f.usuario_id
JOIN Criptomoeda cr ON cr.id = f.criptomoeda_id
ORDER BY u.id, cr.nome;

SELECT
    cr.nome AS criptomoeda,
    cr.simbolo,
    co.precoAtual,
    co.variacao24h,
    co.variacao7d,
    co.variacao30d,
    co.dataConsulta
FROM Cotacao co
JOIN Criptomoeda cr ON cr.id = co.criptomoeda_id
ORDER BY cr.simbolo, co.dataConsulta;

SELECT
    c.id AS carteira_id,
    SUM(ap.valor) AS total_aportado
FROM Aporte ap
JOIN Carteira c ON c.id = ap.carteira_id
GROUP BY c.id
ORDER BY c.id;

COMMIT;
