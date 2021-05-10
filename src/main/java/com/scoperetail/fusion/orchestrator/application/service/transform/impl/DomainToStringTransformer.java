package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DomainToStringTransformer extends AbstractTransformer {

	public DomainToStringTransformer(final DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(final Event event, final Object object, final String templateName) {
		String result = object.toString();
		try {
			result = JsonUtils.marshal(Optional.ofNullable(object));
		} catch (final IOException e) {
			log.error("Unable to transform object: {}", object);
		}
		return result;
	}

}
