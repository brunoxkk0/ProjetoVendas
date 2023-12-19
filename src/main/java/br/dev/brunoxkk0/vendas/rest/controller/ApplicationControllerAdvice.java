package br.dev.brunoxkk0.vendas.rest.controller;

import br.dev.brunoxkk0.vendas.exception.PedidoNaoEncontradoException;
import br.dev.brunoxkk0.vendas.exception.RegraDeNegocioException;
import br.dev.brunoxkk0.vendas.rest.ApiErrors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraDeNegocioException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraDeNegocioException(RegraDeNegocioException exception){
        return new ApiErrors(exception.getMessage());
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handlePedidoNaoEncontradoException(PedidoNaoEncontradoException exception){
        return new ApiErrors(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException exception){
        return new ApiErrors(exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
    }

}
