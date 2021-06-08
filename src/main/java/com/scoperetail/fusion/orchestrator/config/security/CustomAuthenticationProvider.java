package com.scoperetail.fusion.orchestrator.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import com.scoperetail.fusion.core.common.HashUtil;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final String hash;

  CustomAuthenticationProvider(String hash) {
    this.hash = hash;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    if(!isValidCredentials(username+":"+password)) {
      throw new BadCredentialsException("Unauthorized");
    }
    List<GrantedAuthority> authorities = new ArrayList<>();
    return new UsernamePasswordAuthenticationToken(username, password, authorities);
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return aClass.equals(UsernamePasswordAuthenticationToken.class);
  }

  private boolean isValidCredentials(String credentials) {
    String value = HashUtil.getHash(credentials, HashUtil.SHA_256);
    return this.hash.equals(value);
  }
}
