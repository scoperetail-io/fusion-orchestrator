/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import static com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper.HASH_KEY_TEMPLATE;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.common.HashUtil;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;
import com.scoperetail.fusion.shared.kernel.events.Event;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainToDomainEventJsonTransformer extends AbstractTransformer {

  public DomainToDomainEventJsonTransformer(final DomainHelper domainHelper) {
    super(domainHelper);
  }

  @Override
  public String transform(final Event event, final Map<String,Object> params, final String templateName)
      throws Exception {
    String keyHash = null;
    Map<String, String> keyMap = null;
    Object domainEntity=params.get(DOMAIN_ENTITY);
	final String payload = JsonUtils.marshal(Optional.ofNullable(domainEntity));
    final String keyJson = getTextFromTemplate(event, params, HASH_KEY_TEMPLATE);
    keyMap = getkeyMap(keyJson);
    keyHash = HashUtil.getHash(keyJson, HashUtil.SHA3_512);

    final DomainEvent domainEvent =
        DomainEvent.builder().eventId(keyHash).event(event).keyMap(keyMap).payload(payload).build();
    final String result = JsonUtils.marshal(Optional.ofNullable(domainEvent));
    log.trace("Event: {} transformed to domain entity", event);
    return result;
  }
}
