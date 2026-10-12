package com.mercantil.produto;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

	private final ProdutoRepository repository;

	public ProdutoService(ProdutoRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<Produto> listar() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Produto buscar(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
	}

	@Transactional
	public Produto salvar(Produto produto) {
		return repository.save(produto);
	}

	@Transactional
	public void excluir(Long id) {
		repository.deleteById(id);
	}

}
