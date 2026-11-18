# VOLTZ — Missão Tio Patinhas

Gerenciador de carteiras de criptomoedas em **Java**, executado no console e com persistência em banco de dados **Oracle** via **JDBC**. Projeto acadêmico da FIAP.

## Sobre o projeto

O sistema cadastra usuários, carteiras, criptomoedas, ativos, transações, aportes, favoritos e cotações. Cada um desses módulos tem um menu próprio com as operações de inclusão, alteração, exclusão e consulta, todas gravadas diretamente no Oracle.

## Funcionalidades

O menu principal dá acesso a oito módulos:

| Módulo | Tabela | O que representa |
| --- | --- | --- |
| Usuários | `USUARIO` | Pessoas cadastradas no sistema |
| Carteiras | `CARTEIRA` | Carteira de investimentos de um usuário |
| Criptomoedas | `CRIPTOMOEDA` | Moedas disponíveis para negociação |
| Ativos | `ATIVO` | Quantidade de uma criptomoeda em uma carteira |
| Transações | `TRANSACAO` | Compras e vendas realizadas |
| Aportes | `APORTE` | Depósitos feitos em uma carteira |
| Favoritos | `FAVORITO` | Criptomoedas acompanhadas por um usuário |
| Cotações | `COTACAO` | Preço e variação de uma criptomoeda |

Cada módulo abre um submenu com as mesmas opções:

1. Incluir
2. Alterar
3. Excluir
4. Exibir todos
5. Exibir um registro

Três módulos têm uma opção a mais:

- **Carteiras:** recalcular os valores da carteira a partir dos seus ativos.
- **Ativos:** atualizar o valor atual dos ativos pelas últimas cotações.
- **Transações:** exibir as transações de uma carteira.

Os IDs são gerados pelo sistema. Na alteração, pressionar ENTER mantém o valor atual do campo.

## Validações

- Opções de menu, IDs e números inválidos são recusados, e a pergunta é repetida.
- Campos obrigatórios e tamanho máximo dos textos seguem as colunas do banco.
- CPF com 11 dígitos e dígitos verificadores corretos, sem duplicidade.
- E-mail em formato válido e sem duplicidade; telefone com DDD.
- Senha com no mínimo 6 caracteres, gravada apenas como hash SHA-256.
- Símbolo de criptomoeda único, com 2 a 10 letras ou números.
- Valores em reais maiores que zero, com até 2 casas decimais; quantidades com até 8 casas.
- Variações percentuais não menores que -100%.
- Chaves estrangeiras escolhidas a partir da lista de registros existentes.
- Uma carteira não pode ter dois ativos da mesma criptomoeda, e um usuário não pode favoritar a mesma criptomoeda duas vezes.
- Registros com dependentes (por exemplo, um usuário com carteiras) não podem ser excluídos.
- Toda exclusão pede confirmação.

## Tecnologias

- Java 17
- Oracle Database
- JDBC (driver `ojdbc8` 23.2.0.0)
- Maven

## Estrutura do projeto

```
voltz-tiopatinhas/
├── ddl.sql                  Criação das tabelas e alterações
├── dml.sql                  Dados de exemplo e consultas
├── pom.xml                  Configuração do Maven
├── DOCUMENTACAO.md          Modelagem e diagrama de classes
└── src/missao/tiopatinhas/
    ├── Main.java            Programa principal e menu de módulos
    ├── model/               Classes de domínio
    ├── dao/                 Acesso ao banco, uma classe por tabela
    ├── view/                Submenus de console, um por módulo
    ├── util/                Leitura do teclado e validações
    └── factory/             Conexão com o Oracle
```

| Pacote | Classes |
| --- | --- |
| `model` | `Usuario`, `Carteira`, `Criptomoeda`, `Ativo`, `Transacao` (abstrata), `Compra`, `Venda`, `Aporte`, `Favorito`, `Cotacao` |
| `dao` | `UsuarioDao`, `CarteiraDao`, `CriptomoedaDao`, `AtivoDao`, `TransacaoDao`, `AporteDao`, `FavoritoDao`, `CotacaoDao` |
| `view` | `MenuBase` (abstrata) e um menu por módulo: `UsuarioMenu`, `CarteiraMenu`, `CriptomoedaMenu`, `AtivoMenu`, `TransacaoMenu`, `AporteMenu`, `FavoritoMenu`, `CotacaoMenu` |
| `util` | `Console`, `Validador` |
| `factory` | `ConnectionFactory` |

Cada DAO oferece inserir, listar, buscar por ID, alterar e excluir, sempre com `PreparedStatement`. O `Main` abre uma única conexão, compartilhada por todos os DAOs e fechada ao sair do programa.

## Banco de dados

O `ddl.sql` cria as oito tabelas, com chaves primárias e estrangeiras, e em seguida aplica as alterações: colunas obrigatórias, restrições `UNIQUE` e `CHECK`, a coluna `dataAtualizacao` da carteira e a ampliação da coluna `quantidade` para 8 casas decimais.

O `dml.sql` carrega dados de exemplo e traz consultas de conferência.

> **Atenção:** o `dml.sql` começa com comandos `DELETE` que apagam todos os registros das oito tabelas. Execute-o apenas em um esquema de testes.

## Como executar

### Pré-requisitos

- JDK 17 ou superior
- Maven (já incluído no IntelliJ IDEA)
- Usuário e senha do banco Oracle da FIAP

### 1. Preparar o banco

Em um cliente SQL (SQL Developer, DBeaver ou outro), conectado ao seu usuário Oracle, execute na ordem:

1. `ddl.sql`
2. `dml.sql` (opcional, para começar com dados de exemplo)

Se as tabelas já existem de uma fase anterior, execute apenas os dois últimos `ALTER TABLE` da seção `ALTER` do `ddl.sql`, que ampliam a coluna `quantidade` de `Ativo` e `Transacao`.

### 2. Configurar as credenciais

O programa lê o usuário e a senha de variáveis de ambiente. Não há credenciais no código.

| Variável | Conteúdo |
| --- | --- |
| `FIAP_DB_USER` | Usuário do Oracle |
| `FIAP_DB_PASSWORD` | Senha do Oracle |
| `FIAP_DB_URL` | Opcional. Substitui a URL padrão `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` |

No IntelliJ: **Run > Edit Configurations…**, crie uma configuração **Application** com a classe principal `missao.tiopatinhas.Main` e preencha o campo **Environment variables**.

### 3. Executar

Abra o projeto no IntelliJ, aguarde o Maven baixar as dependências e execute a classe `missao.tiopatinhas.Main`. Navegue pelos menus digitando o número da opção.

As inclusões, alterações e exclusões feitas pelos menus são gravadas imediatamente no banco.

## Integrantes

| Nome | RM |
| --- | --- |
| Miguel de França Monteiro | RM566226 |
| Isa | RM562859 |
| Gabriel Dantas Borges Soares | RM564860 |
| João Vitor Lombardi Hayashi | RM563329 |
| Maria Eduarda Santos Pedro | RM566546 |

## Documentação complementar

O arquivo [DOCUMENTACAO.md](DOCUMENTACAO.md) descreve a modelagem das entidades, os relacionamentos e o diagrama de classes.
