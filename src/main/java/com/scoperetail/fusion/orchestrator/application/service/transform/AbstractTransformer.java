package com.scoperetail.fusion.orchestrator.application.service.transform;

import java.util.Map;
import com.scoperetail.fusion.orchestrator.application.service.transform.template.engine.TemplateEngine;
import com.scoperetail.fusion.shared.kernel.events.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractTransformer implements Transformer {

  protected TemplateEngine templateEngine;

  @Override
  public String transform(final Event event, final Map<String, Object> params,
      final String template) throws Exception{
    return templateEngine.generateTextFromTemplate(event, params, template);
  }
}
