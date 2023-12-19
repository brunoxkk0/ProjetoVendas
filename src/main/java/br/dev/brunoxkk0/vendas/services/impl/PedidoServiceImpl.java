package br.dev.brunoxkk0.vendas.services.impl;

import br.dev.brunoxkk0.vendas.domain.entity.Cliente;
import br.dev.brunoxkk0.vendas.domain.entity.ItemPedido;
import br.dev.brunoxkk0.vendas.domain.entity.Pedido;
import br.dev.brunoxkk0.vendas.domain.enums.StatusPedido;
import br.dev.brunoxkk0.vendas.domain.repository.ClienteRepository;
import br.dev.brunoxkk0.vendas.domain.repository.ItemPedidoRepository;
import br.dev.brunoxkk0.vendas.domain.repository.PedidoRepository;
import br.dev.brunoxkk0.vendas.domain.repository.ProdutoRepository;
import br.dev.brunoxkk0.vendas.exception.PedidoNaoEncontradoException;
import br.dev.brunoxkk0.vendas.exception.RegraDeNegocioException;
import br.dev.brunoxkk0.vendas.rest.dto.ItemPedidoDTO;
import br.dev.brunoxkk0.vendas.rest.dto.PedidoDTO;
import br.dev.brunoxkk0.vendas.services.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO pedidoDTO) {

        Pedido pedido = new Pedido();

        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setData(LocalDate.now());
        pedido.setStatus(StatusPedido.REALIZADO);

        int client = pedidoDTO.getCliente();
        Cliente cliente = clienteRepository
                .findById(client)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Código de cliente inválido.")
                );

        pedido.setCliente(cliente);

        List<ItemPedido> itemsPedido = converterItems(pedido, pedidoDTO.getItems());

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);

        pedido.setItems(itemsPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(int id) {
        return pedidoRepository.findByIdFetchItems(id);

    }

    @Override
    @Transactional
    public void atualizaStatus(int id, StatusPedido statusPedido) {
        pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return pedidoRepository.save(pedido);
        }).orElseThrow(PedidoNaoEncontradoException::new);
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> pedidoDTOList) {

        if (pedidoDTOList.isEmpty())
            throw new RegraDeNegocioException("Não é possível criar um pedido sem items.");

        return pedidoDTOList.stream().map(item -> {

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setQuantidade(item.getQuantidade());
            itemPedido.setProduto(produtoRepository.findById(item.getProduto()).orElseThrow(
                    () -> new RegraDeNegocioException(String.format("Produto não cadastrado. Produto: %d", item.getProduto()))
            ));

            return itemPedido;

        }).toList();
    }

}
