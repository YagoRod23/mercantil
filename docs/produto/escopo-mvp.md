# Escopo do MVP

> **Status:** rascunho. Este documento será preenchido depois da reunião de alinhamento entre Yago e Alysson. Até lá, nada aqui é decisão tomada.

## Usuários

Quem vai usar o sistema no dia a dia (dono, caixa, repositor...) e o que cada um precisa fazer.

**Pendente.**

## Objetivos

O que o mercantil precisa resolver primeiro com o sistema.

**Pendente.**

## Funcionalidades do MVP

| Funcionalidade | Entra no MVP? | Responsável | Spec |
|---|---|---|---|
| Cadastro de produtos | Pendente | Pendente | — |
| Estoque | Pendente | Pendente | — |
| Vendas | Pendente | Pendente | — |
| Clientes e fiado | Pendente | Pendente | — |
| Caixa | Pendente | Pendente | — |
| Compras | Pendente | Pendente | — |
| Usuários e permissões | Pendente | Pendente | — |
| Relatórios | Pendente | Pendente | — |

Ordem sugerida no resumo do projeto: produtos → estoque → venda → clientes/fiado → caixa → compras e relatórios. A proposta detalhada, com classes e métodos por fase, está no [roteiro](roteiro.md).

## Fora do MVP

O que fica explicitamente para depois.

**Pendente.**

## Decisões a tomar

Lista da seção 9 do [resumo do projeto](<../../Mercantil — Resumo do projeto.md>). Marque cada uma quando for decidida e registre a resposta aqui (ou em um ADR, se for decisão de arquitetura).

- [ ] Operações indispensáveis no MVP e o que fica para depois
- [ ] Uma loja ou mais de uma
- [ ] Funcionamento sem internet; servidor no mesmo local da loja
- [ ] Controle interno apenas ou integração fiscal
- [ ] Formas de pagamento (dinheiro, Pix, cartão, fiado), troco, pagamento dividido, cancelamento e devolução
- [ ] Unidades de venda (unidade, peso, volume, fracionado); uso de balança e leitor de código de barras
- [ ] Acerto do fiado no caixa e tratamento de atrasos
- [ ] Papéis de usuário e permissões
- [ ] Cadastro inicial: manual, planilha ou ambos
- [ ] Hospedagem, acesso, backup e recuperação
- [ ] Fluxo de trabalho no GitHub: branches, issues, pull requests e revisões
- [ ] Thymeleaf confirmado ou API separada de um frontend JavaScript
