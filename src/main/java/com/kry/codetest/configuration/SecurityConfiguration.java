package com.kry.codetest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                .and()
                .authorizeExchange()
                .pathMatchers("/","/resources/**", "/resources/static/**","/resources/static/dist/**", "/resources/webapp/**")
                .permitAll()
                .pathMatchers("/services/**").hasAuthority("ROLE_USER")
                .pathMatchers("/services/**").hasAuthority("ROLE_ADMIN")
                .pathMatchers("/actuator/**").hasAuthority("ROLE_ADMIN")
                .anyExchange().authenticated()
                .and().formLogin()
                .and().build();
    }

    /**
     * Just for the sake of simplicity I am using an inmemory user details provider.
     * This could be replaced by a keycloak adapter, OpenID Provider or OAuth2 Server Authenticator.
     *
     * @param encoder The password encoder
     * @return A Map based implementation of ReactiveUserDetailsService
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        var user = User
                .withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        var admin = User
                .withUsername("admin")
                .password(encoder.encode("password"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
