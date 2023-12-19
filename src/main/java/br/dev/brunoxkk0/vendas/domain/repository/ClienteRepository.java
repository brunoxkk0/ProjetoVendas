package br.dev.brunoxkk0.vendas.domain.repository;

import br.dev.brunoxkk0.vendas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("select c from Cliente c left join fetch c.pedidos where c.id = :id")
    Cliente findClienteFetchPedido( @Param("id") Integer id );

}
