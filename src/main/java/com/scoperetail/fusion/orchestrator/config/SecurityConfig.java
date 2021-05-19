/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

  @Autowired HTTPInterceptor httpInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(httpInterceptor);
  }
}
