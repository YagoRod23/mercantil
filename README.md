# Mercantil

Sistema web para o controle de um mercantil de bairro: produtos, estoque, vendas, fiado, caixa e compras, tudo em um só lugar.

> **Status:** em fase inicial. O esqueleto do projeto está pronto e o escopo do MVP está sendo definido. Nenhuma funcionalidade está liberada para uso ainda.

## O que o sistema vai fazer

| Módulo | Para que serve |
|---|---|
| **Produtos** | Cadastro, busca, preço, unidade de venda e código de barras |
| **Estoque** | Saldo e histórico de entradas, saídas e ajustes, com motivo |
| **Vendas** | Itens, quantidades, preços e total; venda a prazo vinculada a um cliente |
| **Clientes e fiado** | Cadastro de clientes, vendas a prazo, datas combinadas de pagamento e controle do que foi pago |
| **Caixa** | Abertura, movimentações (incluindo o recebimento de fiado), fechamento e conferência |
| **Compras** | Fornecedores e recebimento de mercadorias |
| **Usuários** | Login, papéis e permissões |
| **Relatórios** | Vendas, posição de estoque e histórico de fiado |

A lista acima é a direção inicial. O que entra no MVP e as regras de negócio ainda estão sendo confirmados. Veja o [resumo do projeto](<Mercantil — Resumo do projeto.md>) para o contexto completo e as decisões pendentes.

## Stack

- **Java 21** e **Spring Boot 4.1**
- **Spring MVC + Thymeleaf**: telas renderizadas no servidor, sem frontend separado
- **PostgreSQL** com **Spring Data JPA**
- **Flyway** para versionar o banco
- **Spring Security** para autenticação e permissões
- **Bean Validation** para validar entradas
- **Maven** (via Maven Wrapper, não é preciso instalar)

A arquitetura é um **monólito modular**: cada domínio do negócio tem seu próprio pacote, e as regras de negócio ficam nos serviços, não nos controllers nem nos templates.

## Como rodar localmente

### 1. Pré-requisitos

- **JDK 21.** Confira com `java -version`. Se aparecer outra versão, instale o JDK 21 e aponte o `JAVA_HOME` para ele.
- **PostgreSQL** rodando na sua máquina (porta 5432).
- **Git.**

### 2. Clonar o repositório

```bash
git clone https://github.com/YagoRod23/mercantil.git
cd mercantil
```

### 3. Criar o banco de dados

```bash
psql -h localhost -U postgres -c "CREATE DATABASE mercantil"
```

As tabelas são criadas automaticamente pelo Flyway na primeira vez que a aplicação sobe.

### 4. Configurar a conexão (se precisar)

Por padrão, a aplicação conecta em `localhost:5432/mercantil` com o usuário `postgres` e a senha `postgres`. Se o seu Postgres usa outros dados, defina as variáveis de ambiente antes de rodar:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/mercantil
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
```

> Os valores padrão servem apenas para desenvolvimento. Em qualquer servidor real, a senha deve vir sempre pela variável `DB_PASSWORD`.

### 5. Subir a aplicação

```bash
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

A aplicação fica disponível em http://localhost:8080.

> Enquanto o módulo de usuários não existe, o Spring Security bloqueia todas as páginas. O log da inicialização mostra uma senha temporária (`Using generated security password: ...`) para entrar com o usuário `user`.

### 6. Rodar os testes

```bash
./mvnw test
```

Os testes também usam o banco local, então o Postgres precisa estar rodando.

## Estrutura do projeto

```text
src/main/java/com/mercantil/
├── MercantilApplication.java
├── produto/        cadastro de produtos
├── estoque/        saldo e movimentações
├── venda/          vendas e itens
├── cliente/        clientes e fiado
├── caixa/          abertura, movimentos e fechamento
├── compra/         fornecedores e recebimento
└── usuario/        login e permissões

src/main/resources/
├── db/migration/   migrações Flyway (V1__..., V2__...)
├── templates/      telas Thymeleaf
├── static/         CSS, JS e imagens
└── application.properties

.claude/skills/     skills do Claude Code usadas no projeto
```

## Como trabalhamos

O projeto segue **desenvolvimento orientado a especificações**: nenhuma funcionalidade é implementada antes de ter uma especificação aprovada.

1. Escrever a especificação em `docs/specs/NNNN-nome-da-funcionalidade/spec.md`: problema, escopo, regras de negócio, fluxos e critérios de aceitação.
2. Escrever o plano em `plano.md`: arquivos, migrações e testes afetados.
3. Revisar e aprovar a especificação e o plano entre os colaboradores.
4. Implementar uma fatia pequena, com testes.
5. Rodar `./mvnw test` e conferir os critérios de aceitação.
6. Atualizar a documentação e o [GitHub Project](https://github.com/users/YagoRod23/projects/1).

Regras que valem para todo o código:

- **Toda mudança no banco vira uma migração Flyway nova.** Nunca edite uma migração que já foi aplicada.
- **Dinheiro é `BigDecimal`** no Java e `NUMERIC` no banco, nunca `double` ou `float`.
- **Vendas concluídas não são apagadas.** Cancelamentos são registrados com motivo, responsável e data/hora.

## Claude Code

O repositório traz skills de projeto em `.claude/skills/`, carregadas automaticamente quando o Claude Code é aberto na pasta do projeto:

- **`frontend-design`**: orienta a criação das telas Thymeleaf.
- **`supabase-postgres-best-practices`**: boas práticas de Postgres para tabelas, migrações, índices e consultas.

Para conferir se elas foram carregadas, digite `/` no Claude Code e procure os nomes na lista.

## Equipe

- [Yago Rodrigues (@YagoRod23)](https://github.com/YagoRod23)
- [Alysson José (@LimaAlysson)](https://github.com/LimaAlysson)
