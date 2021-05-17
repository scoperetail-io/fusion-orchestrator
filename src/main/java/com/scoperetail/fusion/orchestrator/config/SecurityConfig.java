/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(VaultConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final VaultConfig vaultConfig;

  public SecurityConfig(VaultConfig vaultConfig) {
    super();
    this.vaultConfig = vaultConfig;
  }

  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser(vaultConfig.getUsername())
        .password(passwordEncoder().encode(vaultConfig.getPassword()))
        .roles("USER");
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/actuator/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

  // Spring boot needs BCryptPasswordEncoder for encoding user passwords.
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
