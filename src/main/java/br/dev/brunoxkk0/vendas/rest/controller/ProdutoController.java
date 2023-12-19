package br.dev.brunoxkk0.vendas.rest.controller;

import br.dev.brunoxkk0.vendas.domain.entity.Cliente;
import br.dev.brunoxkk0.vendas.domain.entity.Produto;
import br.dev.brunoxkk0.vendas.domain.repository.ProdutoRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable("id") int id, @RequestBody @Valid Produto produto){
        produtoRepository.findById(id).map(el -> {
            produto.setId(el.getId());
            produtoRepository.save(produto);
            return produto;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProdutoById(@PathVariable("id") int id) {
        produtoRepository.findById(id).map(el -> {
            produtoRepository.delete(el);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping("{id}")
    public Produto getProdutoById(@PathVariable("id") int id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    public List<Produto> getProdutoById(@Nullable @RequestParam(required = false) Produto produto) {

        if (!ObjectUtils.isEmpty(produto)) {

            ExampleMatcher matcher = ExampleMatcher
                    .matchingAny()
                    .withIgnoreCase()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreNullValues();

            Example<Produto> produtoExample = Example.of(produto, matcher);

            return produtoRepository.findAll(produtoExample);
        }

        return produtoRepository.findAll();

    }

}
