# Projeto Voltz - Missão Tio Patinhas

Sistema de gerenciamento de carteira de investimentos em criptomoedas desenvolvido em Java com conceitos avançados de orientação a objetos.

## Objetivo

Modelar um sistema completo de controle de investimentos em criptomoedas aplicando herança, polimorfismo, relacionamentos e tratamento de exceções.

## Arquitetura

- **model**: Classes de domínio (entidades e relacionamentos)
- **service**: Serviços de negócio (API de cotações)

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

## Como Executar

Compilar e executar `Main.java`. A saída exibe os testes de criação de objetos, cálculos de valores, transações, aportes e favoritos.
