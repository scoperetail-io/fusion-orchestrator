package com.scoperetail.fusion.orchestrator.application.service.transform.template.engine;

import java.util.Map;
import com.scoperetail.fusion.shared.kernel.events.Event;

public interface TemplateEngine {
  String TEMPLATES = "templates";

  String generateTextFromTemplate(Event event, Map<String, Object> params, String templateName);

}
