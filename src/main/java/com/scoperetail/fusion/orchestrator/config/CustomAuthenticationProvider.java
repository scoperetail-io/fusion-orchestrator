package com.scoperetail.fusion.orchestrator.config;

import com.scoperetail.fusion.orchestrator.common.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Value("${fusion.credentials}")
    private String credentials;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(!validateCredentials(username.strip()+":"+password.strip())) {
            throw new BadCredentialsException("Unauthorized");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean validateCredentials(String credentials) {
        String value = HashUtil.getHash(credentials, HashUtil.SHA_256);
        if (!this.credentials.equals(value)) {
            log.error("Unauthorized credentials: {}", credentials);
            return false;
        }
        return true;
    }
}
