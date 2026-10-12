# Especificações

Cada funcionalidade tem uma pasta própria, numerada em sequência:

```text
docs/specs/
├── 0001-cadastro-de-produtos/
│   ├── spec.md     o que fazer e por quê
│   └── plano.md    como fazer: arquivos, migrações e testes
└── 0002-.../
```

Para começar uma nova, copie `_modelo/` para `NNNN-nome-da-funcionalidade/` usando o próximo número livre.

Uma spec só vai para implementação depois de aprovada pelos dois colaboradores. Dúvidas que mudam o comportamento do mercantil são resolvidas antes, não durante o código.
