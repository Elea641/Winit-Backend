package com.cda.winit.auth.application;

import com.cda.winit.auth.domain.entity.enumeration.Role;
import com.cda.winit.auth.domain.service.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // You can disable csrf protection by removing this line
                        .ignoringRequestMatchers("/api/auth/register", "/api/auth/login")
                        .disable()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/uploads/**",
                                "/api/auth/password-forgotten/**",
                                "/api/auth/new-password/**",
                                "/v3/api-docs/**",
                                "/swagger**",
                                "/swagger-ui/**"
                        ).permitAll()
                        .requestMatchers("/api/sports/**").permitAll()
                        .requestMatchers("/api/teams/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/members/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/tournaments/").permitAll()
                        .requestMatchers("/api/tournaments/generated").permitAll()
                        .requestMatchers("/api/tournaments/current").permitAll()
                        .requestMatchers("/api/tournaments/user").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/tournaments/{tournamentId}").permitAll()
                        .requestMatchers("/api/tournaments/filter").permitAll()
                        .requestMatchers("/api/tournaments/create").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/tournaments/{tournamentId}").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/tournaments/{tournamentName}").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/tournaments/teams/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/users").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/matches/").permitAll()
                        .requestMatchers("/api/matches/{matchId}").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/admin/**").hasAnyRole(Role.ADMIN.name())
                        .requestMatchers("/api/pagination/**").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}