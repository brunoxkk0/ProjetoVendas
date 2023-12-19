package br.dev.brunoxkk0.vendas.services;

import br.dev.brunoxkk0.vendas.domain.entity.Pedido;
import br.dev.brunoxkk0.vendas.domain.enums.StatusPedido;
import br.dev.brunoxkk0.vendas.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO pedidoDTO);

    Optional<Pedido> obterPedidoCompleto( int id);

    void atualizaStatus(int id, StatusPedido statusPedido);

}
