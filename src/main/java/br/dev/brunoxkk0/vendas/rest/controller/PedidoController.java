package br.dev.brunoxkk0.vendas.rest.controller;

import br.dev.brunoxkk0.vendas.domain.entity.Pedido;
import br.dev.brunoxkk0.vendas.domain.enums.StatusPedido;
import br.dev.brunoxkk0.vendas.rest.dto.AtualizacaoStatusPedidoDTO;
import br.dev.brunoxkk0.vendas.rest.dto.InformacoesItemPedidoDTO;
import br.dev.brunoxkk0.vendas.rest.dto.InformacoesPedidoDTO;
import br.dev.brunoxkk0.vendas.rest.dto.PedidoDTO;
import br.dev.brunoxkk0.vendas.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public int save(@RequestBody @Valid PedidoDTO pedidoDTO) {
        return pedidoService.salvar(pedidoDTO).getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable(name = "id") int id) {
        return pedidoService
                .obterPedidoCompleto(id)
                .map(PedidoController::converterPedido)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado.")
                );
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable(name = "id") int id, @RequestBody AtualizacaoStatusPedidoDTO statusPedidoDTO) {
        StatusPedido statusPedido = StatusPedido.valueOf(statusPedidoDTO.getNovo_status());
        pedidoService.atualizaStatus(id, statusPedido);
    }

    private static InformacoesPedidoDTO converterPedido(Pedido el) {
        return InformacoesPedidoDTO.builder()
                .codigo(el.getId())
                .nomeCliente(el.getCliente().getNome())
                .cpfCliente(el.getCliente().getCpf())
                .status(el.getStatus().name())
                .dataPedido(el.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .total(el.getTotal())
                .items(convertItems(el)).build();
    }

    private static List<InformacoesItemPedidoDTO> convertItems(Pedido el) {

        if (CollectionUtils.isEmpty(el.getItems()))
            return Collections.emptyList();

        return el.getItems().stream().map(item -> InformacoesItemPedidoDTO.builder()
                .preco_unitario(item.getProduto().getPreco())
                .quantidade(item.getQuantidade())
                .descricao(item.getProduto().getDescricao()).build()).toList();
    }


}
