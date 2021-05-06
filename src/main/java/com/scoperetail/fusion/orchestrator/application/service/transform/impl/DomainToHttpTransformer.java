package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DomainToHttpTransformer extends AbstractTransformer {

	public DomainToHttpTransformer(DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(Event event, Object object, String templateName) {
		return getTextFromTemplate(event, object, templateName);
	}

}
