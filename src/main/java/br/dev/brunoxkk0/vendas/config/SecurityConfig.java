package br.dev.brunoxkk0.vendas.config;

import br.dev.brunoxkk0.vendas.security.jwt.JwtAuthFilter;
import br.dev.brunoxkk0.vendas.security.jwt.JwtService;
import br.dev.brunoxkk0.vendas.services.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UsuarioServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public OncePerRequestFilter jtwFilter() {
        return new JwtAuthFilter(jwtService, userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(__ -> {
                    __.requestMatchers(antMatcher("/api/produtos/**"))
                            .hasRole("ADMIN");
                    __.requestMatchers(antMatcher("/api/clientes/**"))
                            .hasAnyRole("USER", "ADMIN");
                    __.requestMatchers(antMatcher("/api/pedidos/**"))
                            .hasAnyRole("USER", "ADMIN");
                    __.requestMatchers(
                            antMatcher("/webjars/**"),
                            antMatcher("/v3/api-docs/**"),
                            antMatcher("/swagger-ui/**"),
                            antMatcher("/swagger-ui.html"),
                            antMatcher("/webjars/swagger-ui/**"))
                            .permitAll();
                    __.requestMatchers(antMatcher(POST, "/api/usuarios/**"))
                            .permitAll();
                    __.anyRequest()
                            .authenticated();
                })
                .sessionManagement(__ -> {
                    __.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jtwFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
