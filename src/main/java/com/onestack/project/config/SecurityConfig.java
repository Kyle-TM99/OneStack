package com.onestack.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http
					.requiresChannel(channel -> channel
							.anyRequest().requiresSecure()    // 모든 요청을 HTTPS로 리다이렉트
					)
					.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(auth -> auth
							.requestMatchers("/login/**", "/oauth2/**", "/google/**", "/loginForm/**", 
														 "/chat/**", "/estimation/**", "/**").permitAll()
							.anyRequest().authenticated()
					)
					.oauth2Login(oauth2 -> oauth2
							.loginPage("/loginForm")
							.successHandler((request, response, authentication) -> {
									OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
									String email = oauth2User.getAttribute("email");
									log.info("OAuth2 Login Success. Email: {}", email);
									request.setAttribute("oauth2User", oauth2User);
									request.getRequestDispatcher("/google/callback").forward(request, response);
							})
							.failureHandler((request, response, exception) -> {
									log.error("OAuth2 Login Failed: {}", exception.getMessage());
									request.getRequestDispatcher("/loginForm?error=true").forward(request, response);
							})
							.userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService())
							)
					);
	
			return http.build();
	}
	
	// OAuth2 사용자 서비스 설정을 위한 빈 등록
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		// 기본 OAuth2 사용자 서비스 객체 생성
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		
		// OAuth2 사용자 정보를 로드하는 로직
		return (userRequest) -> {
			// 기본 서비스를 통해 OAuth2 사용자 정보를 가져옴
			OAuth2User oauth2User = delegate.loadUser(userRequest);
			return oauth2User;
		};
	}
}