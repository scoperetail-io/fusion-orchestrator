package com.scoperetail.fusion.orchestrator.domain.shared.kernel.helper;

import java.io.File;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class DomainHelper {

	private static final String TEMPLATES = "templates";

	private static final String HASH_KEY_VM = "hash_key.vm";

	private VelocityEngine velocityEngine;

	// private Configuration configuration;

//	public String getHashKey(final Object domainEntity) throws IOException, TemplateException {
//		configuration.setClassForTemplateLoading(DomainHelper.class, File.separator + TEMPLATES);
//		String domainName = domainEntity.getClass().getSimpleName();
//		Template hashKeyTemplate = configuration.getTemplate(domainName + File.separator + HASH_KEY_FTL);
//		return FreeMarkerTemplateUtils.processTemplateIntoString(hashKeyTemplate, domainEntity);
//	}

	public String getHashKey(final Object domainEntity) {
		String domainName = domainEntity.getClass().getSimpleName();
		String path = TEMPLATES + File.separator + domainName + File.separator + HASH_KEY_VM;
		Template template = velocityEngine.getTemplate(path);
		VelocityContext context = new VelocityContext();
		context.put(domainName, domainEntity);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

}
