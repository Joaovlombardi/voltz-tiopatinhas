# Projeto Voltz - Missão Tio Patinhas

Sistema de gerenciamento de carteira de investimentos em criptomoedas desenvolvido em Java com conceitos avançados de orientação a objetos.

## Objetivo

Modelar um sistema completo de controle de investimentos em criptomoedas aplicando herança, polimorfismo, relacionamentos e tratamento de exceções.

## Arquitetura

- **model**: Classes de domínio (entidades e relacionamentos)
- **service**: Serviços de negócio (API de cotações)
- **dao**: Acesso a dados — CRUD via JDBC no banco Oracle
- **factory**: Criação da conexão com o banco de dados Oracle

## Classes

- **Usuario**: Usuário do sistema com múltiplas carteiras e favoritos
- **Carteira**: Agrega ativos, transações e aportes de um usuário
- **Criptomoeda**: Define criptomoedas com símbolo e descrição
- **Ativo**: Relacionamento many-to-many entre Carteira e Criptomoeda
- **Transacao** (abstrata): Superclasse para operações com métodos abstratos
- **Compra**: Subclasse de Transacao que implementa transações de compra
- **Aporte**: Registro de investimentos na carteira
- **Favorito**: Relacionamento many-to-many entre Usuario e Criptomoeda
- **Cotacao**: Preço e variações de uma criptomoeda
- **ApiCotacaoService**: Simulação de serviço externo de cotações
- **ConnectionFactory**: Cria a conexão JDBC com o Oracle da FIAP, lendo credenciais das variáveis de ambiente `FIAP_DB_USER` e `FIAP_DB_PASSWORD`
- **CriptomoedaDao**: Implementa o CRUD (inserir, listar, alterar, excluir) de `Criptomoeda` no banco de dados

## Conceitos Implementados

### Herança e Polimorfismo Dinâmico
- Classe abstrata `Transacao` define métodos abstratos `calcularValorTotal()` e `exibirDados()`
- Subclasse `Compra` implementa os métodos abstratos com `@Override`
- Polimorfismo dinâmico: um objeto `Transacao` pode referenciar uma instância de `Compra`

### Polimorfismo Estático (Overload)
- `Usuario.exibirDados()` e `Usuario.exibirDados(boolean detalhado)`
- `Compra.exibirDados()` e `Compra.exibirDados(boolean detalhado)`
- `Carteira.adicionarAporte(Aporte)` e `Carteira.adicionarAporte(Aporte, boolean mostrarMensagem)`

### Relacionamentos
- **One-to-Many**: Usuario → Carteira, Carteira → Ativo/Transacao/Aporte, Criptomoeda → Cotacao
- **Many-to-Many**: Usuario ↔ Criptomoeda (via Favorito), Carteira ↔ Criptomoeda (via Ativo)

### Tratamento de Exceções
- Blocos try-catch na Main para capturar exceções
- Relatório de execução com status de sucesso ou erro

### Estruturas de Dados e Arquivos
- Uso de `ArrayList` e `HashMap` na Main para armazenar e consultar usuários e criptomoedas
- Gravação e leitura dos dados em arquivo texto (`dados_voltz.txt`)

## Persistência em Banco de Dados (Oracle)

- **ddl.sql**: script de criação das 8 tabelas (`Usuario`, `Carteira`, `Criptomoeda`, `Ativo`, `Transacao`, `Favorito`, `Cotacao`, `Aporte`) com PKs e FKs, além de exemplos de `ALTER TABLE` (constraints `UNIQUE`/`CHECK`, colunas adicionais) e `DROP TABLE`
- **dml.sql**: script de carga inicial de dados e exemplos de `INSERT`, `UPDATE`, `DELETE` e `SELECT` (incluindo consultas com `JOIN` e `GROUP BY`) para validar a modelagem
- **ConnectionFactory**: conexão JDBC com o Oracle da FIAP (`jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`), driver `ojdbc8` declarado no `pom.xml`
- **CriptomoedaDao**: única classe com integração completa ao banco (inserir, listar, alterar, excluir), conforme escopo desta sprint
- As credenciais do banco não ficam no código — são lidas das variáveis de ambiente `FIAP_DB_USER` e `FIAP_DB_PASSWORD`

## Como Executar

1. Configure as variáveis de ambiente `FIAP_DB_USER` e `FIAP_DB_PASSWORD` com as credenciais do banco Oracle da FIAP.
2. Execute os scripts `ddl.sql` e `dml.sql` no banco para criar e popular as tabelas.
3. Compile e execute `Main.java`.

A saída exibe os testes de criação de objetos, cálculos de valores, transações, aportes e favoritos, a manipulação de `ArrayList`/`HashMap` e arquivo texto, e por fim o teste de integração com o banco (`testarCrudCriptomoeda`), que executa um ciclo completo de INSERT, SELECT, UPDATE, SELECT, DELETE e SELECT sobre a tabela `Criptomoeda`.
