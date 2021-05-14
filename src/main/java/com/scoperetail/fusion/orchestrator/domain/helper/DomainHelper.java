/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.domain.helper;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import com.scoperetail.fusion.orchestrator.config.plugins.commons.TimeZoneCustomizer;
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

	private static final String TEMPLATES = "templates";

	public static final String HASH_KEY_TEMPLATE = "hash_key.vm";

	public static final String OUTBOUND_XML_TEMPLATE = "outbound.vm";

	private VelocityEngine velocityEngine;

	public String generateTextFromTemplate(final Event event, Map<String, Object> params, final String templateName) {
		final String path = TEMPLATES + File.separator + event.name() + File.separator + templateName;
		final Template template = velocityEngine.getTemplate(path);
		final VelocityContext context = new VelocityContext();
		params.forEach((k, v) -> context.put(k, v));
		context.put("tzCustomizer", new TimeZoneCustomizer());
		final StringWriter writer = new StringWriter();
		template.merge(context, writer);
		final String text = writer.toString();
		log.trace("Generated text for \nEvent: {} \nTemplate: {} \nText: {}", event, templateName, text);
		return text;
	}
}
