/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainToTemplateTransformer extends AbstractTransformer {

  public DomainToTemplateTransformer(final DomainHelper domainHelper) {
    super(domainHelper);
  }

  @Override
  public String transform(final Event event, final Map<String, Object> params, final String templateName) {
    final String text = domainHelper.generateTextFromTemplate(event, params, templateName);
    log.trace("Event: {} transformed based on template: {}", event, templateName);
    return text;
  }
}
