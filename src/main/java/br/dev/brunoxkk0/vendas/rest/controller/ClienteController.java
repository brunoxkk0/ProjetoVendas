package br.dev.brunoxkk0.vendas.rest.controller;

import br.dev.brunoxkk0.vendas.domain.entity.Cliente;
import br.dev.brunoxkk0.vendas.domain.repository.ClienteRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Api Clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("{id}")
    @Operation(summary = "Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado para o ID informado"
            )
    })
    public Cliente getClientById(@PathVariable("id") @Parameter(description = "Id do cliente") int id) {
        return clienteRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente salvo com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação"
            )
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClientById(@PathVariable("id") int id) {
        clienteRepository.findById(id).map(el -> {
            clienteRepository.delete(el);
            return el;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateClient(@PathVariable("id") int id, @RequestBody @Valid Cliente cliente) {
        clienteRepository.findById(id).map(el -> {
            cliente.setId(el.getId());
            clienteRepository.save(cliente);
            return cliente;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    public List<Cliente> getClientById(@Nullable @RequestParam(required = false) Cliente cliente) {

        if (!ObjectUtils.isEmpty(cliente)) {

            ExampleMatcher matcher = ExampleMatcher
                    .matchingAny()
                    .withIgnoreCase()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreNullValues();

            Example<Cliente> clienteExample = Example.of(cliente, matcher);

            return clienteRepository.findAll(clienteExample);
        }

        return clienteRepository.findAll();

    }


}
