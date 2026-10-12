# CLAUDE.md

Sistema web para um mercantil de bairro, feito por YagoRod23 e LimaAlysson (Alysson José). O contexto completo e as decisões pendentes estão em `Mercantil — Resumo do projeto.md`; o escopo do MVP fica em `docs/produto/escopo-mvp.md`.

## Stack

Java 21, Spring Boot 4.1, Maven Wrapper, Spring MVC + Thymeleaf, PostgreSQL, Spring Data JPA, Flyway, Spring Security, Bean Validation, JUnit.

## Comandos

```bash
./mvnw spring-boot:run   # sobe em http://localhost:8080
./mvnw test              # testes (precisam do Postgres local rodando)
./mvnw compile           # só compila
```

- Exige JDK 21. Se `java -version` mostrar outra versão, aponte `JAVA_HOME` para um JDK 21 antes de rodar o Maven.
- Banco local: `localhost:5432/mercantil`, configurável por `DB_URL`, `DB_USER` e `DB_PASSWORD`.

## Estrutura

- Monólito modular: um pacote por domínio em `src/main/java/com/mercantil/` (`produto`, `estoque`, `venda`, `cliente`, `caixa`, `compra`, `usuario`).
- Cada domínio segue `Entidade`, `EntidadeRepository`, `EntidadeService`, `EntidadeController`.
- Regras de negócio ficam nos services, nunca em controllers ou templates.
- Migrações em `src/main/resources/db/migration/`, telas em `templates/`.
- Documentação em `docs/`: `produto/` (escopo), `specs/` (uma pasta por funcionalidade), `adr/` (decisões de arquitetura).

## Fluxo de trabalho

1. **Especificar antes de implementar.** Toda funcionalidade tem `docs/specs/NNNN-nome/spec.md` e `plano.md`, a partir do modelo em `docs/specs/_modelo/`.
2. Não edite código antes de o plano ser aprovado pelo usuário.
3. Se uma regra puder mudar o comportamento do mercantil (preço, estoque, caixa, fiado, permissões), pergunte em vez de presumir. Princípios ainda não confirmados com o negócio não são requisitos.
4. Implemente só a fatia aprovada, com testes, e rode `./mvnw test` antes de dar por concluído.
5. Atualize a documentação afetada (spec, escopo, ADR, README).

## Regras do código

- Toda mudança estrutural no banco é uma migração Flyway nova (`V<n>__descricao.sql`). Nunca edite uma migração já aplicada.
- `spring.jpa.hibernate.ddl-auto=validate`: as entidades precisam bater com as migrações.
- Dinheiro: `BigDecimal` no Java e `NUMERIC` no banco. Nunca `double` ou `float`.
- Vendas concluídas não são apagadas: cancelamento/estorno com motivo, usuário e data/hora.
- Venda, baixa de estoque e movimento de caixa acontecem na mesma transação.
- Código, nomes, mensagens e documentação em português.

## Git

- Repositório: github.com/YagoRod23/mercantil, branch `main`. O GitHub é a fonte oficial; não presuma que arquivos locais do outro colaborador estejam sincronizados.
- Mensagens de commit em português, com um título curto no imperativo.
- Não faça commit nem push sem o usuário pedir.

## Skills do projeto (`.claude/skills/`)

- `frontend-design`: ao criar ou alterar telas Thymeleaf.
- `supabase-postgres-best-practices`: ao criar tabelas, migrações, índices ou consultas.
