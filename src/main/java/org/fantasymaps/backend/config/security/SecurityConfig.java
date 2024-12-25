package org.fantasymaps.backend.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.SessionRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SessionRepository<?> sessionRepository) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/maps", "/map/*", "/user", "/tags", "/maps/bundled/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/authenticate", "/logout", "/user/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/maps/manage/creator/{id}", "/bundles/manage/creator/{id}").hasAnyAuthority("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/map", "/bundle").hasAnyAuthority("CREATOR")
                        .requestMatchers(HttpMethod.DELETE, "/map/{id}").hasAnyAuthority("CREATOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/maps/favorite").hasAnyAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/map/*/favorite").hasAnyAuthority("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .logout((logout) -> logout
                        .addLogoutHandler((request, response, authentication) -> {
                            String sessionId = request.getHeader("X-Auth-Token");
                            if (sessionId != null) sessionRepository.deleteById(sessionId);
                            request.getSession().invalidate();
                        })
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                )
                .addFilterBefore(new SessionTokenFilter(sessionRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

