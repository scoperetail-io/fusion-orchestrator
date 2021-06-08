package com.scoperetail.fusion.orchestrator.config;

import javax.xml.validation.Schema;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xml.sax.SAXException;
import com.scoperetail.fusion.core.FusionCoreConfig;
import com.scoperetail.fusion.core.common.JaxbUtil;
import lombok.AllArgsConstructor;

@Configuration
@EnableConfigurationProperties
@AllArgsConstructor
@Import({FusionCoreConfig.class})
public class OrchestratorConfig {

  private static final String ORDER_MESSAGE_XSD = "Order_Message.xsd";

  @Bean
  public Schema orderMessageXmlSchema() throws SAXException {
    return JaxbUtil.getSchema(this.getClass(), ORDER_MESSAGE_XSD);
  }

}
