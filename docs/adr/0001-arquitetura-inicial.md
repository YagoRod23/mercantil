# ADR 0001 — Arquitetura inicial

- **Status:** aceita como base inicial
- **Data:** 12/10/2026
- **Decidido por:** Yago Rodrigues (a revisar com Alysson José na reunião de alinhamento)

## Contexto

O Mercantil é um sistema web para um mercantil de bairro, desenvolvido por duas pessoas, com Claude como assistente. O escopo do MVP ainda não foi fechado. A equipe precisava de uma base técnica para começar sem se comprometer com estruturas que só fariam sentido em sistemas grandes.

## Opções avaliadas

1. **Monólito modular em Spring Boot com telas Thymeleaf.** Um único projeto e um único deploy; cada domínio em seu pacote.
2. **API Spring Boot + frontend JavaScript separado.** Mais flexível para apps futuros, mas são dois projetos, dois builds e mais coisa para manter com uma equipe de duas pessoas.
3. **Microserviços.** Descartado: não existe uma necessidade concreta (escala, equipes independentes) que justifique o custo.

## Decisão

Adotar a opção 1:

- Java 21 e Spring Boot em versão estável (sem SNAPSHOT ou milestone), com Maven Wrapper.
- Monólito modular organizado por domínio: `produto`, `estoque`, `venda`, `cliente`, `caixa`, `compra`, `usuario`.
- Spring MVC + Thymeleaf para as telas.
- PostgreSQL com Spring Data JPA; esquema versionado pelo Flyway, com o Hibernate apenas validando (`ddl-auto=validate`).
- Spring Security para autenticação e autorização; Bean Validation nas entradas.
- Regras de negócio nos services, nunca em controllers ou templates.

## Consequências

- Um só projeto para configurar, testar e implantar.
- Os limites entre domínios dependem de disciplina: convém que um módulo não acesse diretamente o repositório de outro e use o service dele.
- Se no futuro houver necessidade de um app ou de integrações externas, controllers REST podem ser adicionados ao lado das telas sem reescrever os services.

## Pendências

- A escolha entre Thymeleaf e API + frontend JavaScript ainda está na lista de decisões do [escopo do MVP](../produto/escopo-mvp.md). Se mudar, registrar em um novo ADR.
- O rascunho atual de `ProdutoController` expõe REST/JSON e foi criado antes desta decisão. Ele deve ser revisto na spec de cadastro de produtos.
