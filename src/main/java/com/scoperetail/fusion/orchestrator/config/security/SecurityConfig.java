package com.scoperetail.fusion.orchestrator.config.security;

import com.scoperetail.fusion.messaging.config.FusionConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final FusionConfig fusionConfig;

	SecurityConfig(FusionConfig fusionConfig) {
		this.fusionConfig = fusionConfig;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().httpBasic().and().authorizeRequests()
				.antMatchers("/actuator/**").permitAll().anyRequest().authenticated()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		CustomAuthenticationProvider customAuthenticationProvider =
				new CustomAuthenticationProvider(fusionConfig.getCredentials().getHash());
		auth.authenticationProvider(customAuthenticationProvider);
	}

}
