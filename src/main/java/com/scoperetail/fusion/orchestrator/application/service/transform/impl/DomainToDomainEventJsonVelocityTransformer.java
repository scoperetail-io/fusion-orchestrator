/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import org.springframework.stereotype.Component;
import com.scoperetail.fusion.orchestrator.application.service.transform.template.engine.VelocityTemplateEngine;

@Component
public class DomainToDomainEventJsonVelocityTransformer
    extends AbstractDomainToDomainEventJsonTransformer {

  public DomainToDomainEventJsonVelocityTransformer(final VelocityTemplateEngine templateEngine) {
    super(templateEngine);
  }
}
