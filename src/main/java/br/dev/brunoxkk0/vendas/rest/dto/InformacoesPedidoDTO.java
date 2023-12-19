package br.dev.brunoxkk0.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesPedidoDTO {

    private int codigo;
    private String nomeCliente;
    private String cpfCliente;
    private String dataPedido;
    private BigDecimal total;
    private String status;
    private List<InformacoesItemPedidoDTO> items;

}
