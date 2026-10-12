# Mercantil — Resumo do projeto

> Documento de contexto inicial para orientar YagoRod23, https://github.com/LimaAlysson e Claude Cowork/Claude Code. Foi escrito com as informações disponíveis em 12/10/2026. O que ainda não foi decidido está marcado como **Pendente**; recomendações técnicas não devem ser confundidas com requisitos já confirmados.

## 1. Visão do projeto

O **Mercantil** será um sistema web para apoiar o controle de um mercantil de bairro. O projeto será desenvolvido em conjunto por **YagoRod23** e https://github.com/LimaAlysson. A aplicação ainda está em fase inicial de planejamento e não há funcionalidades implementadas confirmadas.

O objetivo imediato é definir escopo e regras de negócio e, depois, desenvolver o sistema em fatias pequenas, documentadas e testadas. A ideia é usar o VS Code como IDE e Claude como assistente de desenvolvimento, com desenvolvimento orientado a especificações.

## 2. Colaboração e repositórios

- Repositório de código: [github.com/YagoRod23/mercantil](https://github.com/YagoRod23/mercantil)
- GitHub Project para acompanhar o trabalho: [github.com/users/YagoRod23/projects/1](https://github.com/users/YagoRod23/projects/1)
- Responsáveis: YagoRod23 e https://github.com/LimaAlysson.
- Pasta de trabalho local informada: `Área de Trabalho/mercantil` (o caminho absoluto depende do sistema operacional e do nome da pasta Desktop do computador).
- O VS Code já foi preparado pelo responsável pelo projeto.
- Conferência do GitHub feita em 12/10/2026: o repositório é público, a branch padrão é `main`, e a árvore visível contém apenas `README.md`. O README está vazio ou sem conteúdo textual retornado. O Project `mercantil` está público, tem 13 campos configurados e nenhum item cadastrado. Essas informações podem mudar; consultem GitHub para o estado atual.

**Fonte oficial do trabalho:** o GitHub deve ser tratado como a fonte compartilhada do código, das especificações e das decisões registradas. Não presumir que conversas individuais no Claude ou arquivos locais do computador do outro colaborador estejam sincronizados.

## 3. Stack acordada como direção inicial

O responsável escolheu desenvolver com **Java e Spring Boot**. A proposta técnica abaixo foi aceita como base inicial de trabalho, mas versões e dependências devem ser conferidas no Spring Initializr no momento de gerar o projeto.

- Java 21.
- Spring Boot, usando versão estável — evitar versões `SNAPSHOT` ou `M`/milestone.
- Maven e Maven Wrapper (`mvnw`/`mvnw.cmd`).
- Spring MVC e Thymeleaf para uma aplicação web sem frontend separado inicialmente.
- PostgreSQL.
- Spring Data JPA para persistência.
- Flyway para versionar migrações do banco.
- Bean Validation para validar entradas.
- Spring Security para autenticação e autorização.
- JUnit e ferramentas de teste do ecossistema Spring.

**Arquitetura recomendada:** monólito modular organizado por domínio de negócio. Não iniciar com microserviços sem uma necessidade concreta. Regras de negócio devem ficar em serviços/módulos, e não em controllers ou templates.

Estrutura Java sugerida (a adaptar ao gerar o projeto):

```text
src/main/java/com/mercantil/
├── MercantilApplication.java
├── produto/
├── estoque/
├── venda/
├── cliente/
├── caixa/
├── compra/
└── usuario/

src/main/resources/
├── db/migration/
├── static/
└── templates/

docs/
├── produto/
├── specs/
└── adr/
```

## 4. Domínios funcionais considerados

São áreas candidatas para o sistema, ainda sujeitas à confirmação do responsável e do colaborador:

- **Produtos:** cadastro, busca, preço, unidade de venda e código de barras.
- **Estoque:** saldo e registro de entradas, saídas, ajustes e motivos.
- **Vendas:** identificação de itens, quantidades, preços e total, com suporte à vinculação do cliente em caso de prazo.
- **Clientes e Fiado (Contas a Receber):** cadastro de clientes, gestão de vendas a prazo ("fiado"), registro de datas pré-determinadas para pagamento (ex: dias de recebimento de salário do cliente) e controle do que já foi pago ou está pendente.
- **Caixa:** abertura, movimentações (incluindo o recebimento e acerto de fiados), fechamento e conferência.
- **Compras:** fornecedores e recebimento de mercadorias.
- **Usuários:** autenticação, papéis e permissões.
- **Relatórios:** vendas, posição de estoque e histórico de fiados/contas a receber.

Uma sequência inicial possível é: cadastro de produtos → estoque → venda → clientes/fiado → caixa → compras e relatórios. Essa ordem é uma recomendação, não uma decisão definitiva.

## 5. Princípios de negócio a validar

As regras seguintes foram propostas como pontos de atenção e devem ser confirmadas com quem opera o mercantil:

1. Uma venda concluída deve preservar os itens, quantidades e preços praticados no momento da venda.
2. A gravação da venda, a baixa de estoque e a movimentação correspondente do caixa precisam manter consistência transacional.
3. Em vez de apagar vendas concluídas, avaliar cancelamento/estorno com motivo, usuário responsável e data/hora.
4. Armazenar valores monetários em centavos inteiros ou em um tipo decimal adequado; não depender de ponto flutuante binário para cálculos de dinheiro.
5. Definir permissões distintas para operar venda, ajustar estoque, cancelar venda e fechar caixa.
6. Toda alteração estrutural do banco deve ser acompanhada por migração Flyway versionada.
7. Vendas na modalidade "fiado" exigem, obrigatoriamente, a vinculação a um cliente cadastrado e o estabelecimento de uma data/condição prevista para pagamento (ex: dia do recebimento de salário ou data fixa combinada).
8. O acerto (pagamento) de um "fiado" deve ser registrado como uma entrada real no caixa no momento do recebimento da dívida, mantendo o histórico de quando a venda ocorreu.

Esses itens ainda não substituem requisitos fiscais, contábeis ou operacionais e não devem ser apresentados como decisões do negócio até serem confirmados.

## 6. Como trabalhar com Claude: especificar antes de implementar

Cada funcionalidade deve ter uma especificação curta antes do código. Uma pasta por funcionalidade pode seguir este modelo:

```text
docs/specs/0001-cadastro-de-produtos/
├── spec.md
└── plano.md
```

A especificação deve registrar:

- problema e objetivo do usuário;
- escopo e itens fora de escopo;
- atores e permissões;
- regras de negócio;
- fluxo principal e casos de erro;
- critérios de aceitação verificáveis;
- impacto esperado em dados, páginas, endpoints e testes;
- dúvidas que precisam de decisão humana.

Fluxo de trabalho recomendado:

1. Explorar o código e as especificações existentes, sem editar.
2. Elaborar/revisar a especificação e resolver dúvidas que mudem comportamento.
3. Pedir ao Claude um plano com arquivos, migrações e testes afetados.
4. Revisar e aprovar o plano antes da implementação.
5. Implementar uma fatia vertical pequena.
6. Rodar testes e build Maven; conferir evidências e critérios de aceitação.
7. Atualizar documentação e abrir/atualizar um item no GitHub Project.
8. Revisar a mudança com o outro colaborador e fazer commit/PR de acordo com o processo que vocês definirem.

Prompt de início para uma funcionalidade:

> Leia `CLAUDE.md`, o escopo do produto e as especificações relacionadas. Primeiro explore o projeto e proponha uma especificação e um plano com mudanças em Java, templates, migrações Flyway e testes. Não edite arquivos antes de eu aprovar. Se uma regra puder alterar o comportamento do mercantil, pergunte em vez de presumir. Depois da aprovação, implemente apenas a fatia acordada e execute os testes relevantes.

## 7. Instruções persistentes e Skills

### `CLAUDE.md`

Criar na raiz do repositório:

```text
mercantil/CLAUDE.md
```

Esse arquivo deve conter o contexto curto que Claude Code precisa em todas as sessões: stack, estrutura, comandos de build/teste, regras de contribuição e convenções que se aplicam ao repositório inteiro. Deve ser versionado no Git para que os dois colaboradores recebam as mesmas instruções.

### Skills de projeto para Claude Code

Skills compartilhadas pelo repositório devem ficar sob a pasta oculta `.claude/skills/`, dentro da raiz do projeto. Cada Skill tem sua própria pasta e um `SKILL.md`:

```text
mercantil/
└── .claude/
    └── skills/
        └── especificar-funcionalidade/
            └── SKILL.md
```

O arquivo da Skill deve explicar quando ela se aplica e o procedimento que Claude deve seguir. Ao versionar `.claude/skills/` no Git, os dois desenvolvedores terão os arquivos no clone do repositório e o Claude Code poderá carregá-los quando trabalhar nesse projeto.

Skills candidatas para criar depois, quando o processo estiver claro:

- `especificar-funcionalidade`: criar/revisar `spec.md` e critérios de aceitação.
- `implementar-funcionalidade`: seguir especificação aprovada, incluir migração/testes e verificar build.
- `revisar-alteracao`: revisar diff, regras de negócio, segurança e testes.

Não é necessário criar todas agora. Começar com uma Skill de especificação é suficiente; ajustar depois com exemplos reais.

### Skills no Claude Cowork são uma instalação separada

Não presumir que o Claude Cowork vá carregar automaticamente as Skills commitadas no diretório `.claude/skills/` do repositório. Para usar uma Skill personalizada no Claude/Cowork, a documentação indica carregá-la na área **Customize > Skills** como um ZIP contendo a pasta da Skill, e então habilitá-la. Essa configuração é independente do arquivo no Git.

Se os dois colaboradores quiserem usar uma Skill personalizada em contas Cowork individuais, cada um pode precisar instalá-la na própria conta, a menos que o plano/organização de vocês permita compartilhar ou provisionar Skills. Manter a cópia-fonte versionada no Git ajuda a revisar e exportar a mesma Skill; o ZIP é uma cópia de distribuição.

### Contexto do projeto no Cowork

Para uma conversa Cowork entender o projeto, anexar este resumo como conhecimento/arquivo do projeto Cowork ou fornecer o arquivo à conversa. Também se pode definir instruções do projeto com regras de comportamento. Se o Cowork estiver ligado a uma pasta local, mantenham no repositório qualquer conteúdo que queiram preservar; a ligação ao projeto Cowork não substitui o versionamento no Git.

## 8. Estrutura documental recomendada

```text
mercantil/
├── CLAUDE.md
├── README.md
├── docs/
│   ├── produto/
│   │   └── escopo-mvp.md
│   ├── specs/
│   │   └── 0001-cadastro-de-produtos/
│   │       ├── spec.md
│   │       └── plano.md
│   └── adr/
│       └── 0001-arquitetura-inicial.md
└── .claude/
    └── skills/
        └── especificar-funcionalidade/
            └── SKILL.md
```

- `README.md`: o que é o sistema, como configurar e executar, e como rodar os testes.
- `CLAUDE.md`: instruções breves para Claude Code em todas as sessões.
- `docs/produto/escopo-mvp.md`: usuários, objetivos, funcionalidades do MVP e exclusões.
- `docs/specs/`: especificações verificáveis por funcionalidade.
- `docs/adr/`: decisões de arquitetura, com contexto, opções avaliadas e decisão.
- `.claude/skills/`: procedimentos reutilizáveis específicos do projeto para Claude Code.

## 9. Decisões ainda pendentes

Antes de implementar os módulos de venda/estoque, alinhar entre os dois colaboradores:

1. Quais operações são indispensáveis no MVP e quais ficam para depois?
2. O sistema será usado em uma loja ou em mais de uma?
3. Precisa funcionar sem internet? O computador da loja fica no mesmo local que o servidor?
4. É apenas controle interno ou haverá integração fiscal/emissão de documento fiscal? Quais obrigações e integração são exigidas pelo negócio?
5. Quais formas de pagamento (dinheiro, Pix, cartão) devem ser registradas no sistema, além do "fiado", e como representar troco, pagamentos divididos, cancelamentos e devoluções?
6. Quais unidades de venda existem: unidade, peso, volume, fracionado? Há balança ou leitor de código de barras?
7. Como será a transição do pagamento das contas a receber (fiado) para a movimentação do caixa no dia do acerto? Como tratar os atrasos nos pagamentos quando a data pré-determinada não for cumprida?
8. Quais papéis de usuário e permissões são necessários?
9. Como será feito o cadastro inicial: manual, importação de planilha ou ambos?
10. Onde o sistema será hospedado, quem terá acesso e como serão feitos backup e recuperação?
11. Quem será o segundo colaborador no GitHub e qual será o fluxo de trabalho: branches, issues, pull requests, revisões e nomes de responsáveis?
12. O frontend Thymeleaf está aprovado ou vocês preferem uma API Spring Boot separada de um frontend JavaScript?

Não é necessário responder a tudo para gerar este resumo. As decisões podem ser tomadas gradualmente, antes da especificação da funcionalidade afetada.

## 10. Próximos passos recomendados

1. Copiar este arquivo para a raiz da pasta local `mercantil` e adicioná-lo ao Git.
2. Criar um projeto no Claude Cowork e adicionar este Markdown à seção de arquivos/conhecimento; iniciar pedindo um resumo do que está decidido e das dúvidas pendentes, sem gerar código.
3. Alinhar entre os dois desenvolvedores o escopo do MVP e responder primeiro às decisões de operação offline, fiscal, pagamentos (incluindo as regras finais do fiado) e uso de leitor/balança.
4. Atualizar o `README.md` do repositório e criar `CLAUDE.md` na raiz.
5. Gerar o esqueleto Spring Boot com Spring Initializr, usando Maven, Java 21 e dependências selecionadas conforme a direção descrita acima.
6. Criar uma primeira especificação pequena, como cadastro de produtos; aprovar especificação e plano antes de pedir código.
7. Criar a primeira Skill do repositório quando o fluxo de especificação estiver funcionando.

## Referências oficiais úteis

- [Spring Initializr](https://start.spring.io/)
- [Requisitos de sistema do Spring Boot](https://docs.spring.io/spring-boot/system-requirements.html)
- [Claude Code no VS Code](https://code.claude.com/docs/en/vs-code)
- [Instruções e memória de projeto do Claude Code](https://code.claude.com/docs/en/memory)
- [Skills do Claude Code](https://code.claude.com/docs/en/skills)
- [Projetos no Claude](https://support.claude.com/en/articles/9519177-how-can-i-create-and-manage-projects)
- [Usar Skills no Claude](https://support.claude.com/en/articles/12512180-use-skills-in-claude)
- [Claude Cowork no desktop, web e mobile](https://support.claude.com/en/articles/15520349-use-claude-cowork-on-web-desktop-and-mobile)
