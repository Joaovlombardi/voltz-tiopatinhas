# VOLTZ — Missão Tio Patinhas

Projeto acadêmico desenvolvido em **Java** para simular o gerenciamento de uma carteira de investimentos em criptomoedas. O sistema reúne conceitos de orientação a objetos, estruturas de dados, manipulação de arquivos e persistência em banco de dados **Oracle** por meio de **JDBC**.

## Objetivo

Modelar usuários, carteiras, criptomoedas e operações financeiras, demonstrando os relacionamentos entre entidades e as operações de cadastro, consulta, atualização e exclusão de dados (**CRUD**).

## Tecnologias utilizadas

- **Java 17** — versão de linguagem configurada no projeto.
- **Maven** — gerenciamento das dependências e compilação.
- **Oracle Database e JDBC** — persistência dos dados.
- **Oracle JDBC (`ojdbc8`, versão `23.2.0.0`)** — driver declarado no `pom.xml`.
- **IntelliJ IDEA** — ambiente utilizado no desenvolvimento e nos testes.
- **SQL** — criação, relacionamento e manipulação das tabelas.

## Estrutura do projeto

| Caminho | Responsabilidade |
| --- | --- |
| `src/missao/tiopatinhas/model/` | Classes de domínio e relacionamentos |
| `src/missao/tiopatinhas/dao/` | Classes DAO responsáveis pelas operações CRUD |
| `src/missao/tiopatinhas/factory/ConnectionFactory.java` | Conexão com o Oracle via JDBC |
| `src/missao/tiopatinhas/service/ApiCotacaoService.java` | Simulação de um serviço de cotações |
| `src/missao/tiopatinhas/Main.java` | Demonstração do sistema e execução dos testes |
| `ddl.sql` | Criação das tabelas, chaves e restrições |
| `dml.sql` | Carga de dados e exemplos de comandos SQL |
| `dados_voltz.txt` | Arquivo utilizado no teste de gravação e leitura |
| `DOCUMENTACAO.md` | Documentação complementar da modelagem |

## Funcionalidades implementadas

### Modelagem e orientação a objetos

- **Usuario:** dados cadastrais, carteiras e criptomoedas favoritas.
- **Carteira:** organização de ativos, transações e aportes; valores investidos e atuais.
- **Criptomoeda:** identificação por nome, símbolo e descrição.
- **Ativo:** vínculo entre uma carteira e uma criptomoeda.
- **Transacao, Compra e Venda:** representação das operações de compra e venda, utilizando herança e polimorfismo.
- **Aporte:** registro de aportes financeiros na carteira.
- **Favorito:** vínculo entre usuário e criptomoeda.
- **Cotacao:** preços e variações de criptomoedas.

O projeto também demonstra **herança, classes abstratas, sobrescrita e sobrecarga de métodos, tratamento de exceções, `ArrayList` e `HashMap`**. O serviço `ApiCotacaoService` simula cotações; não realiza consulta a uma API externa em tempo real.

### Persistência no Oracle — 8 classes DAO

As seguintes classes implementam operações de **inserir, listar, alterar e excluir** registros:

| Classe DAO | Tabela correspondente |
| --- | --- |
| `UsuarioDao` | `USUARIO` |
| `CarteiraDao` | `CARTEIRA` |
| `CriptomoedaDao` | `CRIPTOMOEDA` |
| `AtivoDao` | `ATIVO` |
| `TransacaoDao` | `TRANSACAO` |
| `AporteDao` | `APORTE` |
| `FavoritoDao` | `FAVORITO` |
| `CotacaoDao` | `COTACAO` |

Os relacionamentos são definidos por **chaves primárias e estrangeiras (PKs e FKs)**. O SQL também demonstra restrições como `UNIQUE` e `CHECK`.

## Como executar

### 1. Requisitos

- JDK compatível com **Java 17** ou superior.
- Maven e IDE Java, como o IntelliJ IDEA.
- Acesso autorizado ao banco Oracle da FIAP.
- Tabelas do projeto já criadas no ambiente de testes.

### 2. Preparar o banco, se necessário

O arquivo `ddl.sql` cria as oito tabelas e suas restrições. Execute-o **somente quando estiver preparando um esquema de testes que ainda não contenha essas tabelas**.

O arquivo `dml.sql` contém dados demonstrativos e consultas, mas **começa com comandos `DELETE` que removem todos os registros das oito tabelas**. Utilize-o apenas em um esquema de testes descartável e após conferir seu conteúdo. Não é necessário executá-lo novamente quando o banco já está configurado.

### 3. Configurar o projeto no IntelliJ

1. Abra a pasta do projeto e importe o `pom.xml` como projeto Maven.
2. Carregue as dependências Maven.
3. Crie uma configuração de execução do tipo **Application**, com a classe principal `missao.tiopatinhas.Main`.
4. Configure nessa execução as variáveis de ambiente `FIAP_DB_USER` (usuário Oracle) e `FIAP_DB_PASSWORD` (senha Oracle).

A conexão é realizada por `ConnectionFactory.java`, utilizando a URL `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`. **Não inclua credenciais no código ou no repositório.**

### 4. Compilar e executar

Para compilar sem executar as operações de banco, utilize a tarefa Maven `compile` no IntelliJ ou o comando `mvn compile` no terminal, dentro da pasta do projeto.

Para executar a demonstração, inicie a classe `missao.tiopatinhas.Main` pelo IntelliJ, com as variáveis de ambiente configuradas.

**Atenção:** o `Main.java` executa operações reais de `INSERT`, `SELECT`, `UPDATE` e `DELETE` no Oracle, incluindo exclusões de registros com IDs fixos de teste (95 a 99 e 196). Ele também regrava o arquivo `dados_voltz.txt`. Execute-o **apenas em um ambiente de testes preparado**, após verificar que os registros envolvidos não pertencem a outros usuários.

## Testes e validação

A classe `Main.java` contém rotinas de teste para:

- CRUD de usuário e carteira;
- CRUD de ativo;
- CRUD de transação (compra e venda) e aporte;
- CRUD de favorito e cotação;
- CRUD de criptomoeda;
- manipulação de `ArrayList` e `HashMap`;
- gravação e leitura de `dados_voltz.txt`.

**Validação realizada em 11/10/2026:** conexão JDBC com o Oracle estabelecida, oito tabelas identificadas, operações CRUD demonstradas no console e compilação Maven da branch `master` concluída com `BUILD SUCCESS` após o merge da Fase 6.

As rotinas são demonstrações executadas pelo `Main.java`, e não uma suíte de testes unitários automatizados. Confira as mensagens de cada operação e os possíveis erros no console; a mensagem de encerramento, isoladamente, **não confirma o sucesso de todos os testes**.

## Documentação complementar

Consulte o arquivo `DOCUMENTACAO.md` para informações adicionais sobre a modelagem de entidades, os relacionamentos e o diagrama de classes.