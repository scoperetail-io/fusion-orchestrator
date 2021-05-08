package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

@Component
public class DomainToHttpTransformer extends AbstractTransformer {

	public DomainToHttpTransformer(final DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(final Event event, final Object object, final String templateName) {
		return getTextFromTemplate(event, object, templateName);
	}

}
