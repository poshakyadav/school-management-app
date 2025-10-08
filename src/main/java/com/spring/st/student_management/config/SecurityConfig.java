package com.spring.st.student_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.st.student_management.service.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // Here we need write the Object of UserDetailsService
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        // Here we need write the Object of Password Encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Configurations
        // httpSecurity.authorizeHttpRequests(request -> {
        // request.requestMatchers("/students/**", "/teachers/**", "/user/**",
        // "/adv-fees/**", "/classes/**")
        // .authenticated();
        // request.anyRequest().permitAll();
        // });

        httpSecurity.authorizeHttpRequests(request -> {
            request.requestMatchers("/teachers/**", "/students/**").hasRole("ADMIN");
            request.requestMatchers("/classes/**", "/sections/**", "/schedules/**", "/contact/**")
                    .hasRole("ADMIN");
            request.requestMatchers("/grades/**", "/courses/**").hasAnyRole("TEACHER", "ADMIN");
            request.requestMatchers("/user/**", "/user/profile").hasAnyRole("ADMIN", "STUDENT", "TEACHER");

            request.anyRequest().permitAll();
        });

        // Form login
        // httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/user-login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile", true);
            formLogin.failureForwardUrl("/user-login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        // Add cache control headers to prevent browsers from caching protected pages
        httpSecurity.headers(headers -> {
            headers.cacheControl(cache -> cache.disable());
        });

        httpSecurity.logout(lg -> {
            lg.logoutUrl("/user-logout")
                    .logoutSuccessUrl("/user-login?logout=true")
                    .invalidateHttpSession(true) // Invalidate the session
                    .deleteCookies("JSESSIONID"); // Clear the session cookie
        });

        httpSecurity.sessionManagement(session -> {
            session.invalidSessionUrl("/user-login?sessionExpired=true") // Redirect on session invalidation.
                    .maximumSessions(1) // Allow only one active session.
                    .expiredUrl("/user-login?sessionExpired=true"); // Redirect when a session expires due to new login.
        });

        httpSecurity.sessionManagement(session -> {
            session.invalidSessionUrl("/user-login?sessionExpired=true")
                    .sessionFixation(sessionFixation -> sessionFixation.newSession());
        });

        return httpSecurity.build(); // This build() will return the default SecurityFilterChain.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}