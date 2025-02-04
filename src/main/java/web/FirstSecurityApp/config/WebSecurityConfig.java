package web.FirstSecurityApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import web.FirstSecurityApp.security.AuthProviderImpl;

import java.security.AuthProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthProviderImpl authProvider;

    @Autowired
    public WebSecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }


    //настройка аутентификации
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/hello").hasAnyRole("USER", "ADMIN")  // Только USER и ADMIN
                .requestMatchers("/login", "/registration").permitAll()
                .requestMatchers(HttpMethod.POST, "/registration").permitAll()
                .anyRequest().authenticated()

                .and()

                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/login?error")

                .and()

                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");

        return http.build();
    }
}