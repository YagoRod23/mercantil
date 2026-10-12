# Roteiro de desenvolvimento

> **Status:** proposta para discutir na reunião de alinhamento entre Yago e Alysson. A ordem, as classes e os métodos abaixo são sugestões técnicas; nada aqui confirma regra de negócio. Pontos marcados com ❓ dependem de decisão do mercantil (ver [escopo do MVP](escopo-mvp.md)).

Cada fase vira uma ou mais specs em `docs/specs/NNNN-nome/` (spec + plano aprovados antes do código) e issues no [GitHub Project](https://github.com/users/YagoRod23/projects/1).

## Por que esta ordem

Produtos → estoque → caixa básico → venda → fiado → permissões → relatórios → compras.

O resumo do projeto sugeria venda antes do caixa. Aqui o caixa básico vem antes porque a venda, a baixa de estoque e o movimento de caixa precisam acontecer na mesma transação: para concluir uma venda, o caixa já tem de existir.

## Fase 0 — Fundação

- `config/SecurityConfig`: login por formulário.
- Migração com a tabela `usuario` e um usuário administrador inicial.
- `usuario/Usuario` (login, senhaHash, nome, papel) e `UsuarioService implements UserDetailsService`. Já é necessário aqui porque venda, caixa e estoque registram quem fez cada operação. Papéis e permissões finos ficam para a Fase 6.
- Exceções de domínio `NaoEncontradoException` e `RegraNegocioException`, tratadas por um `GlobalExceptionHandler` (`@ControllerAdvice`).
- `templates/layout.html`: layout base com menu, usado pelas demais telas.

## Fase 1 — Produtos

Já existem `Produto`, `ProdutoRepository`, `ProdutoService`, um `ProdutoController` REST em `/api/produtos` e a migração `V1__criar_tabela_produto.sql`.

- Migração `V2`: coluna `unidade` (enum `UnidadeVenda`: `UN`, `KG`…). ❓ Vende por peso? Há balança? Leitor de código de barras?
- Repository: `findByNomeContainingIgnoreCaseAndAtivoTrue`, `existsByCodigoBarras`.
- Service: `listar(filtro)`, `buscar(id)`, `buscarPorCodigoBarras(codigo)`, `criar(form)` (código de barras único), `atualizar(id, form)`, `inativar(id)`.
- Trocar a exclusão física (`excluir`) por inativação: quando houver vendas apontando para o produto, apagá-lo quebra o histórico.
- Controller Thymeleaf: `GET /produtos`, `GET /produtos/novo`, `POST /produtos`, `GET /produtos/{id}/editar`, `POST /produtos/{id}`, `POST /produtos/{id}/inativar`.
- Testes: `ProdutoServiceTest` e `ProdutoControllerTest` (`@WebMvcTest`).

## Fase 2 — Estoque

- `MovimentoEstoque`: produto, tipo (`ENTRADA`/`SAIDA`/`AJUSTE`), quantidade (`BigDecimal`), motivo, usuário, dataHora.
- Saldo em `produto.estoque_atual`, atualizado na mesma transação do movimento; os movimentos são o histórico.
- `EstoqueService`: `registrarEntrada(produtoId, qtd, motivo)`, `registrarAjuste(produtoId, novoSaldo, motivo)`, `baixarPorVenda(produto, qtd, venda)`, `historico(produtoId)`.
- ❓ Pode vender com estoque negativo?

## Fase 3 — Caixa básico

- `Caixa`: aberto por, abertura, valor inicial, fechamento, valor contado, status.
- `MovimentoCaixa`: tipo (`VENDA`, `RECEBIMENTO_FIADO`, `SANGRIA`, `SUPRIMENTO`), valor, forma de pagamento, venda (opcional), dataHora.
- `CaixaService`: `abrir(valorInicial)`, `caixaAberto()`, `registrarMovimento(...)`, `sangria(...)`, `suprimento(...)`, `fechar(valorContado)` (calcula a diferença).
- ❓ Quais formas de pagamento? Um caixa só ou um por operador?

## Fase 4 — Venda

- `Venda`: status (`ABERTA`/`CONCLUIDA`/`CANCELADA`), total, forma de pagamento, cliente (opcional), operador, motivo/autor/data do cancelamento.
- `ItemVenda`: produto, descrição e preço unitário copiados no momento da venda, quantidade, subtotal.
- Regras na própria entidade: `Venda.adicionarItem()`, `Venda.calcularTotal()`, `ItemVenda.calcularSubtotal()`.
- `VendaService`: `iniciar()`, `adicionarItem(vendaId, codigoBarras, qtd)`, `removerItem(...)`, `concluir(vendaId, pagamento)` — uma única `@Transactional` que chama `EstoqueService` e `CaixaService` —, `cancelar(vendaId, motivo)` com estorno de estoque e caixa.
- Tela de PDV com campo para o código de barras.

## Fase 5 — Clientes e fiado

- `Cliente`: nome, telefone, dia combinado de pagamento.
- `ContaReceber`: cliente, venda, valor, vencimento, status. `PagamentoFiado`: conta, valor, forma, dataHora.
- `ClienteService`: cadastro e busca.
- `FiadoService`: `registrarFiado(venda, cliente, vencimento)`, `receber(contaId, valor, forma)` (gera entrada no caixa), `emAberto(clienteId)`, `vencidas()`.
- ❓ Pagamento parcial? Limite de crédito? Juros ou tratamento de atraso?

## Fase 6 — Papéis e permissões

- Enum `Papel` (ex.: `DONO`, `CAIXA`) e regras por URL ou `@PreAuthorize`: cancelar venda, ajustar estoque, fechar caixa.
- ❓ Quais papéis existem e o que cada um pode fazer?

## Fase 7 — Relatórios

- `RelatorioService`: vendas por período, produtos com estoque baixo, fiados em aberto e vencidos.

## Fase 8 — Compras (provavelmente fora do MVP)

- `Fornecedor`, `Compra`, `ItemCompra`; `CompraService.receber()` chama `EstoqueService.registrarEntrada`.

## Divisão de trabalho (sugestão)

O Alysson quer praticar Java escrevendo o código: Produtos e Clientes são bons para isso, pois são CRUDs completos (entidade, validação, service, tela, teste). Estoque, caixa e venda, que concentram as regras transacionais, podem ficar com o Yago usando Claude Code. Cada um revisa o PR do outro.
