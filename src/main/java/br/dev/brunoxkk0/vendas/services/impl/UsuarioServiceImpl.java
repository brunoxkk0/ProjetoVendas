package br.dev.brunoxkk0.vendas.services.impl;

import br.dev.brunoxkk0.vendas.domain.entity.Usuario;
import br.dev.brunoxkk0.vendas.domain.repository.UsuarioRepository;
import br.dev.brunoxkk0.vendas.exception.SenhaInvalidaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario) {
        UserDetails userDetails = loadUserByUsername(usuario.getLogin());
        boolean passwordMatch = encoder.matches(usuario.getPassword(), userDetails.getPassword());

        if(passwordMatch)
            return userDetails;

        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException("Username " + username + " não encontrado na base de dados.")
        );

        String[] roles = usuario.isAdmin() ?
                new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getPassword())
                .roles(roles)
                .build();
    }

}
