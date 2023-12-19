package br.dev.brunoxkk0.vendas.rest.controller;

import br.dev.brunoxkk0.vendas.domain.entity.Usuario;
import br.dev.brunoxkk0.vendas.exception.SenhaInvalidaException;
import br.dev.brunoxkk0.vendas.rest.dto.CredenciaisDTO;
import br.dev.brunoxkk0.vendas.rest.dto.TokenDTO;
import br.dev.brunoxkk0.vendas.security.jwt.JwtService;
import br.dev.brunoxkk0.vendas.services.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciaisDTO) {
        try {

            Usuario usuario = Usuario.builder()
                    .login(credenciaisDTO.getLogin())
                    .password(credenciaisDTO.getSenha())
                    .build();

            UserDetails autenticar = usuarioService.autenticar(usuario);

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(autenticar.getUsername(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException loginException) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, loginException.getMessage());
        }
    }

}
