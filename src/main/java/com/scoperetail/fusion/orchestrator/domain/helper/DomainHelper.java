package com.scoperetail.fusion.orchestrator.domain.helper;

import java.io.File;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public final class DomainHelper {

	private static final String EVENT = "EVENT";

	private static final String TEMPLATES = "templates";

	public static final String HASH_KEY_TEMPLATE = "hash_key.vm";

	public static final String OUTBOUND_XML_TEMPLATE = "outbound.vm";

	private VelocityEngine velocityEngine;

	public String generateTextFromTemplate(final Event event, final Object domainEntity, final String templateName) {
		final String path = TEMPLATES + File.separator + event.name() + File.separator + templateName;
		final Template template = velocityEngine.getTemplate(path);
		final VelocityContext context = new VelocityContext();
		context.put(EVENT, domainEntity);
		final StringWriter writer = new StringWriter();
		template.merge(context, writer);
		final String text = writer.toString();
		log.trace("Generated text for Event: {} from Template: {} Text: {}", event, template, text);
		return text;
	}

}
