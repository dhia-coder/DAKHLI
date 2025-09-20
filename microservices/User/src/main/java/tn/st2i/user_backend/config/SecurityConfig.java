package tn.st2i.user_backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Require authentication for other endpoints
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login/**").permitAll() // Public endpoints
//                        .requestMatchers("/api/events").permitAll()
//                        .requestMatchers("/api/events/**").permitAll()
//                        .requestMatchers("/api/exam-periods/**").permitAll()
//                        .requestMatchers("/api/exam-periods").permitAll()
//                        .requestMatchers("/api/programmes").permitAll()
//                        .requestMatchers("/api/schedules/**").permitAll()
//
//                                                .requestMatchers("/error").permitAll()
//
//                        .requestMatchers(HttpMethod.GET, "/api/etablissements").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/roles/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/regions").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/regions/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/disciplines").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/disciplines/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/classes").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/classes/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/permissions").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/permissions/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/users/username/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/users", "/api/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll().
//                        requestMatchers(HttpMethod.GET, "/api/users/**").permitAll().
//                        requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
//                        .requestMatchers("/error").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/etablissements/by-region/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/classes/by-etablissement/**").permitAll()
//                        .requestMatchers("/api/programmes/**").permitAll()
//                        .anyRequest().authenticated() // Require authentication for other endpoints
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
  public CorsConfigurationSource corsConfigurationSource() {
  CorsConfiguration configuration = new CorsConfiguration();
  configuration.setAllowedOrigins(List.of("http://localhost:4200"));
 configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
  configuration.setAllowedHeaders(List.of("*"));
   configuration.setAllowCredentials(true);
   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
   return source;
 }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
