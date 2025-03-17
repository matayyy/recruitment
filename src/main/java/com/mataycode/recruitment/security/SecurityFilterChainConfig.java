package com.mataycode.recruitment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider, AuthenticationEntryPoint authenticationEntryPoint, OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/customers").permitAll()
                        .requestMatchers("/api/v1/orders").permitAll()
                        .requestMatchers("/api/v1/products").permitAll()
                        .requestMatchers("/api/v1/customers").permitAll()
                        .requestMatchers("/api/v1/registration").permitAll()
                        .requestMatchers("/api/v1/registration/confirm").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .formLogin(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler));

//                .exceptionHandling(conf -> conf.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }
}
