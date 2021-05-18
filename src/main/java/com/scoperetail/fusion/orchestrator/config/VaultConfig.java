/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("orchestrator")
@Data
public class VaultConfig {
  private String username;
  private String password;
}
