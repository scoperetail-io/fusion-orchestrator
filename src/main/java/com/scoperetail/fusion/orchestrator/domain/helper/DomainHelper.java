/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.domain.helper;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import com.scoperetail.fusion.messaging.config.FusionConfig;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.scoperetail.fusion.shared.kernel.events.Event;

import freemarker.template.TemplateExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Component
@AllArgsConstructor
@Slf4j
public final class DomainHelper {

	private static final String TEMPLATES = "templates";
	private static final String FTL = "ftl";
	private static final String FTL_EXTENSION = ".ftl";
	private static final String VM = "vm";
	private static final String VM_EXTENSION = ".vm";

	public static final String HASH_KEY_TEMPLATE = "hash_key";

	private final FusionConfig fusionConfig;

	private final VelocityEngine velocityEngine;

	private final FreeMarkerConfigurer freeMarkerConfigurer;

	public String generateTextFromTemplate(final Event event, Map<String, Object> params, String templateName) {

		switch (fusionConfig.getTemplateEngine()) {
			case FTL:
				log.trace("FTL transformation");
				return generateTextFromFtl(event, params, templateName);
			case VELOCITY:
				log.trace("VELOCITY transformation");
				return generateTextFromVelocity(event, params, templateName);
			default:
				log.trace("Template Engine not indicated");
				return null;
		}
	}

	private String generateTextFromVelocity(final Event event, Map<String, Object> params, final String templateName) {
		final String path = TEMPLATES + File.separator + event.name() + File.separator + VM + File.separator +
				templateName + VM_EXTENSION;
		final Template template = velocityEngine.getTemplate(path);
		final VelocityContext context = new VelocityContext();
		params.forEach(context::put);
		final StringWriter writer = new StringWriter();
		template.merge(context, writer);
		final String text = writer.toString();
		log.trace("Generated text for \nEvent: {} \nTemplate: {} \nText: {}", event, templateName, text);
		return text;
	}

	private String generateTextFromFtl(final Event event, Map<String, Object> params, String templateName) {
		final String path = event.name() + File.separator + FTL + File.separator + templateName + FTL_EXTENSION;
		freeMarkerConfigurer.getConfiguration().setNumberFormat("computer");
		freeMarkerConfigurer.getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

		try {
			freemarker.template.Template template = freeMarkerConfigurer.getConfiguration()
					.getTemplate(StringUtils.cleanPath(path));
			final StringWriter writer = new StringWriter();
			template.process(params, writer);
			final String text = writer.toString();
			log.trace("Generated text for \nEvent: {} \nTemplate: {} \nText: {}", event, templateName, text);
			return text;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
