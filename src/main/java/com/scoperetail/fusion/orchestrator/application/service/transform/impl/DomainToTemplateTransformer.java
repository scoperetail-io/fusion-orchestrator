package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

@Component
public class DomainToTemplateTransformer extends AbstractTransformer {

	public DomainToTemplateTransformer(final DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(final Event event, final Object entity, final String templateName) {
		return domainHelper.generateTextFromTemplate(event, entity, templateName);
	}
}
