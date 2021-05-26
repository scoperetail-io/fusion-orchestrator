/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.domain.helper;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.scoperetail.fusion.shared.kernel.events.Event;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public final class DomainHelper {

	private static final String TEMPLATES = "templates";

	public static final String HASH_KEY_TEMPLATE = "hash_key.ftl";

	public static final String OUTBOUND_XML_TEMPLATE = "outbound.vm";

	private Configuration freemarkerConfig;

	public String generateTextFromTemplate(final Event event, Map<String, Object> params, String templateName) {
		final String path = event.name() + File.separator + templateName;

		freemarkerConfig.setClassForTemplateLoading(this.getClass(), File.separator + TEMPLATES);
		freemarkerConfig.setIncompatibleImprovements(Configuration.VERSION_2_3_23);
		freemarkerConfig.setDefaultEncoding("UTF-8");
		freemarkerConfig.setNumberFormat("computer");
		freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

		try {
			freemarker.template.Template template = freemarkerConfig.getTemplate(StringUtils.cleanPath(path));
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
