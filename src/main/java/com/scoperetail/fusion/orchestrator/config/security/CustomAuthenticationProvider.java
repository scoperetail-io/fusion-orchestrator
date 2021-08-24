package com.scoperetail.fusion.orchestrator.config.security;

/*-
 * *****
 * fusion-orchestrator
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====
 */

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
