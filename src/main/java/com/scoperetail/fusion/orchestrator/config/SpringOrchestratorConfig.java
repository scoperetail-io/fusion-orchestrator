package com.scoperetail.fusion.orchestrator.config;

import javax.xml.validation.Schema;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.xml.sax.SAXException;

import com.scoperetail.fusion.messaging.schema.util.JaxbUtil;

import lombok.AllArgsConstructor;

@Configuration
@EnableConfigurationProperties
@EnableRetry
@AllArgsConstructor
public class SpringOrchestratorConfig {

	private static final String PICKING_SUB_SYSTEM_ORDER_COMPLETE_MESSAGE_V1_0_XSD = "PickingSubSystemOrderCompleteMessage_V1.0.xsd";
	private static final String PICKING_ORDER_BEGIN_EVENT_V1_0_XSD = "PickingOrderBeginEvent_V1.0.xsd";

	@Bean
	public VelocityEngine getVelocityEngine() throws VelocityException {
		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		return velocityEngine;
	}

	@Bean
	public Schema pickBeginXmlSchema() throws SAXException {
		return JaxbUtil.getSchema(this.getClass(), PICKING_ORDER_BEGIN_EVENT_V1_0_XSD);
	}

	@Bean
	public Schema pickEndXmlSchema() throws SAXException {
		return JaxbUtil.getSchema(this.getClass(), PICKING_SUB_SYSTEM_ORDER_COMPLETE_MESSAGE_V1_0_XSD);
	}
}
