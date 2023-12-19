package br.dev.brunoxkk0.vendas.rest.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {

    private int produto;
    private int quantidade;

}
