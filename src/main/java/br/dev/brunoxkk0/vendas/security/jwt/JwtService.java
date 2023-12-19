package br.dev.brunoxkk0.vendas.security.jwt;

import br.dev.brunoxkk0.vendas.VendasApplication;
import br.dev.brunoxkk0.vendas.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private Long expiracao;

    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario usuario) {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(expiracao);
        Instant instant = expiration.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return Jwts.builder()
                .subject(usuario.getLogin())
                .expiration(date)
                .signWith(SignatureAlgorithm.HS256, chaveAssinatura)
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(chaveAssinatura)
                .build()
                .parseClaimsJws(token)
                .getPayload();

    }

    public boolean tokenValido(String token) {
        try {
            Claims claims = obterClaims(token);
            Date expiracao = claims.getExpiration();
            LocalDateTime localDateTime = expiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return LocalDateTime.now().isBefore(localDateTime);
        } catch (Exception ignored) {
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return obterClaims(token).getSubject();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(VendasApplication.class);
        JwtService jwtService = context.getBean(JwtService.class);
        String token = jwtService.gerarToken(Usuario.builder().login("teste").build());

        System.out.println("O token é válido: " + jwtService.tokenValido(token));
        System.out.println("O token é de: " + jwtService.obterLoginUsuario(token));

    }

}
