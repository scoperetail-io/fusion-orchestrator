package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import static com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper.OUTBOUND_XML_TEMPLATE;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

@Component
public class DomainToXmlTransformer extends AbstractTransformer {

	public DomainToXmlTransformer(final DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(final Event event, final Object entity) throws IOException {
		return domainHelper.generateTextFromTemplate(event, entity, OUTBOUND_XML_TEMPLATE);
	}

}
