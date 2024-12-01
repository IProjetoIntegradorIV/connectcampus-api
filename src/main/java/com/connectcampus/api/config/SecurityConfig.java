package com.connectcampus.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// Declara que esta classe contém definições de configuração para o Spring.
// O Spring processará esta classe para configurar beans e outras dependências.

@EnableWebSecurity
// Habilita o Spring Security para gerenciar e configurar a segurança da aplicação.
// Esta anotação é necessária para ativar os mecanismos de proteção padrão e personalizados do Spring Security.
public class SecurityConfig {
    @Bean
    // Define que este método retorna um bean gerenciado pelo Spring.
    // Aqui, o bean representa a configuração da cadeia de filtros de segurança HTTP.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita a proteção CSRF (Cross-Site Request Forgery).
                // Útil em APIs REST, onde o CSRF não é necessário porque geralmente não utilizam cookies para autenticação.
                .csrf(AbstractHttpConfigurer::disable)

                // Configura as permissões de acesso às requisições HTTP.
                // Neste caso, permite que qualquer requisição seja acessada sem restrição.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest() // Refere-se a qualquer requisição HTTP.
                        .permitAll()); // Permite todas as requisições sem necessidade de autenticação.

        // Constrói e retorna a cadeia de filtros de segurança configurada.
        return http.build();
    }
}
