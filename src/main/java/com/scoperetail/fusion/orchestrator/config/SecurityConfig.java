package com.scoperetail.fusion.orchestrator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${spring.security.user.name:97ce9cc8-8db8-4040-9850-3aed0a95cf06}")
	private String userName;

	@Value("${spring.security.user.password:*Ag#E6MAUHpW8vGL7@$Z}")
	private String userPassword;

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(userName).password(passwordEncoder().encode(userPassword)).roles("USER");
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().httpBasic().and().authorizeRequests().antMatchers("/actuator/**").permitAll().anyRequest()
				.authenticated();
	}

	// Spring boot needs BCryptPasswordEncoder for encoding user passwords.
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
