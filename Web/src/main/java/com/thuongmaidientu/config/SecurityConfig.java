package com.thuongmaidientu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.thuongmaidientu.security.CustomAuthFailureHandler;
import com.thuongmaidientu.security.CustomAuthenticationProvider;
import com.thuongmaidientu.security.CustomLoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationProvider authenticationProvider;

	@Autowired
	private CustomLoginSuccessHandler successHandler;

	@Autowired
	private CustomAuthFailureHandler failureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider)
				.authorizeHttpRequests(
						auth -> auth
						//không cần đối với gọi api
						.requestMatchers("/quan-tri/don-hang/**").permitAll()
						.requestMatchers("/quan-tri/san-pham/**").permitAll()
						.requestMatchers("/quan-tri/san-pham").permitAll()
						.requestMatchers("/quan-tri/banner").permitAll()
						.requestMatchers("/quan-tri/thuoc-tinh").permitAll()
						.requestMatchers("/quan-tri/thuoc-tinh/**").permitAll()
						.requestMatchers("/quan-tri/khach-hang").permitAll()
						.requestMatchers("/quan-tri/khach-hang/**").permitAll()
						.requestMatchers("/quan-tri/danh-gia").permitAll()
						.requestMatchers("/quan-tri/nha-cung-cap").permitAll()
						.requestMatchers("/quan-tri/khu-vuc-kho").permitAll()
						.requestMatchers("/quan-tri/thong-ke").permitAll()
						.requestMatchers("/quan-tri/thong-ke/**").permitAll()
						.requestMatchers("/quan-tri/don-hang").permitAll()

						.requestMatchers("/quan-tri/**").hasRole("ADMIN").anyRequest().permitAll())
				.formLogin(form -> form.loginPage("/dang-nhap").loginProcessingUrl("/process-login")
						.successHandler(successHandler).failureHandler(failureHandler).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
						.logoutSuccessUrl("/trang-chu"))
				.csrf().disable();

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
		return new HandlerMappingIntrospector();
	}

}
