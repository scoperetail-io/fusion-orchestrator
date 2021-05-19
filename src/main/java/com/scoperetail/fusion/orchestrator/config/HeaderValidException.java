/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.config;

public class HeaderValidException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public HeaderValidException() {
    super("header AUTHORIZATION not found");
  }
}
