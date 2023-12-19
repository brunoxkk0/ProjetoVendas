package br.dev.brunoxkk0.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesItemPedidoDTO {
    private String descricao;
    private BigDecimal preco_unitario;
    private int quantidade;
}
