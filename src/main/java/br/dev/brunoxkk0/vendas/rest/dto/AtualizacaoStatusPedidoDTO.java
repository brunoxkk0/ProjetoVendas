package br.dev.brunoxkk0.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoStatusPedidoDTO {

    private String novo_status;

}
