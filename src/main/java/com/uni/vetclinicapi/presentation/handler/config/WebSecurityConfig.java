package com.uni.vetclinicapi.presentation.handler.config;

import com.uni.vetclinicapi.security.filter.JwtAuthenticationTokenFilter;
import com.uni.vetclinicapi.security.util.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * This class contains the configuration for the Web Security and all of the ants and their authority roles.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    private static final String MANAGER_AUTHORITY = "MANAGER";

    private static final String ADMIN_AUTHORITY = "ADMIN";
    private static final String CUSTOMER_AUTHORITY = "CUSTOMER";

    private static final String VET_AUTHORITY = "VET";
    //TODO
    // add the other authorities

    private static final String[] AUTH_WHITELIST = {
            // -- auth
            "/api/v1/auth/*",
            // -- services
            "/api/v1/services"
    };

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers("/api/v1/auth/register","/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/users","/api/v1/users/**").hasAnyAuthority(CUSTOMER_AUTHORITY,ADMIN_AUTHORITY)
                        .requestMatchers("/api/v1/users/vets").hasAnyAuthority(CUSTOMER_AUTHORITY,ADMIN_AUTHORITY)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users").hasAnyAuthority(CUSTOMER_AUTHORITY, ADMIN_AUTHORITY)
                        .requestMatchers("/api/v1/users/pets").hasAuthority(CUSTOMER_AUTHORITY)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/pets").hasAnyAuthority(CUSTOMER_AUTHORITY, ADMIN_AUTHORITY)
                        .requestMatchers("/api/v1/pets").hasAuthority(CUSTOMER_AUTHORITY)
                        .requestMatchers("/api/v1/visits").hasAuthority(CUSTOMER_AUTHORITY)
                        .requestMatchers("/api/v1/meds","/api/v1/meds/**").hasAnyAuthority(ADMIN_AUTHORITY,VET_AUTHORITY)
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/meds/**").hasAuthority(VET_AUTHORITY)
                        .requestMatchers("/api/v1/admin", "/api/v1/admin/**").hasAuthority(ADMIN_AUTHORITY));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}