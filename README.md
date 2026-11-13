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
| `src/missao/tiopatinhas/view/` | Submenus de console, um por módulo (`MenuBase` e classes `*Menu`) |
| `src/missao/tiopatinhas/util/` | Leitura do teclado (`Console`) e regras de validação (`Validador`) |
| `src/missao/tiopatinhas/Main.java` | Programa principal: conexão com o banco e menu de módulos |
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

As seguintes classes implementam operações de **inserir, listar, buscar por ID, alterar e excluir** registros:

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

### Programa de console — Fase 7

O `Main` é um programa interativo. O menu principal dá acesso a oito módulos (Usuários, Carteiras, Criptomoedas, Ativos, Transações, Aportes, Favoritos e Cotações), e cada módulo abre um submenu com:

1. Incluir
2. Alterar
3. Excluir
4. Exibir todos
5. Exibir um registro

Alguns módulos têm opções extras: recalcular os valores da carteira a partir dos ativos, atualizar o valor atual dos ativos pelas últimas cotações e exibir as transações de uma carteira.

Os IDs são gerados automaticamente. Na alteração, pressionar ENTER mantém o valor atual do campo.

**Validações aplicadas:**

- opções de menu, IDs e números inválidos são recusados e a pergunta é repetida;
- campos obrigatórios e tamanho máximo dos textos conforme as colunas do banco;
- CPF com 11 dígitos e dígitos verificadores corretos, sem duplicidade;
- e-mail em formato válido e sem duplicidade; telefone com DDD;
- senha com no mínimo 6 caracteres, gravada apenas como hash SHA-256;
- símbolo de criptomoeda único, com 2 a 10 letras ou números;
- valores em reais maiores que zero, com até 2 casas decimais; quantidades com até 8 casas;
- variações percentuais não menores que -100%;
- chaves estrangeiras escolhidas a partir da lista de registros existentes;
- uma carteira não pode ter dois ativos da mesma criptomoeda, nem um usuário favoritar a mesma criptomoeda duas vezes;
- registros com dependentes (por exemplo, um usuário com carteiras) não podem ser excluídos, e toda exclusão pede confirmação.

## Como executar

### 1. Requisitos

- JDK compatível com **Java 17** ou superior.
- Maven e IDE Java, como o IntelliJ IDEA.
- Acesso autorizado ao banco Oracle da FIAP.
- Tabelas do projeto já criadas no ambiente de testes.

### 2. Preparar o banco, se necessário

O arquivo `ddl.sql` cria as oito tabelas e suas restrições. Execute-o **somente quando estiver preparando um esquema de testes que ainda não contenha essas tabelas**.

Se as tabelas já existem desde a Fase 6, execute apenas os dois `ALTER TABLE ... MODIFY (quantidade NUMBER(21,8))` do final da seção `ALTER` do `ddl.sql`. Eles ampliam a coluna `quantidade` de `Ativo` e `Transacao` para 8 casas decimais; sem eles, o Oracle arredonda as quantidades para 2 casas.

O arquivo `dml.sql` contém dados demonstrativos e consultas, mas **começa com comandos `DELETE` que removem todos os registros das oito tabelas**. Utilize-o apenas em um esquema de testes descartável e após conferir seu conteúdo. Não é necessário executá-lo novamente quando o banco já está configurado.

### 3. Configurar o projeto no IntelliJ

1. Abra a pasta do projeto e importe o `pom.xml` como projeto Maven.
2. Carregue as dependências Maven.
3. Crie uma configuração de execução do tipo **Application**, com a classe principal `missao.tiopatinhas.Main`.
4. Configure nessa execução as variáveis de ambiente `FIAP_DB_USER` (usuário Oracle) e `FIAP_DB_PASSWORD` (senha Oracle).

A conexão é realizada por `ConnectionFactory.java`, utilizando a URL `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`. **Não inclua credenciais no código ou no repositório.**

### 4. Compilar e executar

Para compilar sem executar as operações de banco, utilize a tarefa Maven `compile` no IntelliJ ou o comando `mvn compile` no terminal, dentro da pasta do projeto.

Para usar o programa, inicie a classe `missao.tiopatinhas.Main` pelo IntelliJ, com as variáveis de ambiente configuradas, e navegue pelos menus digitando o número da opção.

**Atenção:** as operações de inclusão, alteração e exclusão feitas pelos menus são gravadas imediatamente no Oracle.

A variável de ambiente opcional `FIAP_DB_URL` permite apontar o programa para outro banco sem alterar o código; quando ela não existe, é usada a URL da FIAP.

## Testes e validação

**Fase 6 (11/10/2026):** conexão JDBC com o Oracle estabelecida, oito tabelas identificadas e operações CRUD demonstradas no console.

**Fase 7 (13/11/2026):** o código foi compilado para Java 17 e os menus foram exercitados de ponta a ponta contra um banco H2 em memória, em modo de compatibilidade com Oracle, carregado com o `dml.sql`: inclusão, alteração, exclusão, exibição de todos e de um registro nos oito módulos, além das entradas inválidas e das exclusões bloqueadas por dependentes. **A execução contra o Oracle da FIAP ainda precisa ser repetida**, pois o H2 não reproduz todos os comportamentos do Oracle.

O projeto não possui uma suíte de testes unitários automatizados.

## Documentação complementar

Consulte o arquivo `DOCUMENTACAO.md` para informações adicionais sobre a modelagem de entidades, os relacionamentos e o diagrama de classes.