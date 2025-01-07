package com.onestack.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {		
		
		http.authorizeHttpRequests(
				authorizeHttpRequests -> 
					authorizeHttpRequests.requestMatchers(
								new AntPathRequestMatcher("/**"))
					.permitAll())
					.csrf(csrf -> csrf.ignoringRequestMatchers(
							new AntPathRequestMatcher("/h2-console/**")))
					.csrf(csrf -> csrf.disable())
					.logout(logout -> logout // logout
					//		.logoutUrl("/logout")
							.logoutSuccessUrl("/loginForm")
							.invalidateHttpSession(true));
		
		return http.build();
	}
}