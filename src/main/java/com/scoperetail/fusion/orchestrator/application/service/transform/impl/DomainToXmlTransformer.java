package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import static com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper.OUTBOUND_XML_TEMPLATE;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DomainToXmlTransformer extends AbstractTransformer {

	public DomainToXmlTransformer(DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(Event event, Object entity) throws IOException {
		log.trace("Processing message {}", event);
		return domainHelper.generateTextFromTemplate(event,entity, OUTBOUND_XML_TEMPLATE);
	}

}
