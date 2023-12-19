package br.dev.brunoxkk0.vendas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.MySQLDialect;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login")
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;

    @Column(name = "senha")
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String password;

    @Column(name = "admin")
    private boolean admin;

}
