package br.dev.brunoxkk0.vendas.domain.repository;

import br.dev.brunoxkk0.vendas.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByLogin(String login);

}
