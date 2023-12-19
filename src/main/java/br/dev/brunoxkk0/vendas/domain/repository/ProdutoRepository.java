package br.dev.brunoxkk0.vendas.domain.repository;

import br.dev.brunoxkk0.vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
