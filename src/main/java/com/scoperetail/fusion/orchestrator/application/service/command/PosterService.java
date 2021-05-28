/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.FAILURE;
import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.SUCCESS;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import com.scoperetail.fusion.messaging.config.Adapter;
import com.scoperetail.fusion.messaging.config.Adapter.TransformationType;
import com.scoperetail.fusion.messaging.config.Adapter.TransportType;
import com.scoperetail.fusion.messaging.config.Config;
import com.scoperetail.fusion.messaging.config.FusionConfig;
import com.scoperetail.fusion.messaging.config.UseCaseConfig;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundJmsPort;
import com.scoperetail.fusion.orchestrator.application.port.out.web.PosterOutboundWebPort;
import com.scoperetail.fusion.orchestrator.application.service.transform.Transformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToDomainEventJsonFtlTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToDomainEventJsonVelocityTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToFtlTemplateTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToStringTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToVelocityTemplateTransformer;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import com.scoperetail.fusion.shared.kernel.events.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@AllArgsConstructor
@Slf4j
class PosterService implements PosterUseCase {

  private final PosterOutboundJmsPort posterOutboundJmsPort;

  private final PosterOutboundWebPort posterOutboundWebPort;

  private final DomainToDomainEventJsonVelocityTransformer domainToDomainEventJsonVelocityTransformer;

  private final DomainToDomainEventJsonFtlTransformer domainToDomainEventJsonFtlTransformer;

  private final DomainToFtlTemplateTransformer domainToFtlTemplateTransformer;

  private final DomainToVelocityTemplateTransformer domainToVelocityTemplateTransformer;

  private final DomainToStringTransformer domainToStringTransformer;

  private final FusionConfig fusionConfig;

  @Override
  public void post(final Event event, final Object domainEntity, final boolean isValid)
      throws Exception {
    handleEvent(event, domainEntity, isValid);
  }

  private void handleEvent(final Event event, final Object domainEntity, final boolean isValid)
      throws Exception {
    final Optional<UseCaseConfig> optUseCase = fusionConfig.getUsecases().stream()
        .filter(u -> u.getName().equals(event.name())).findFirst();
    if (optUseCase.isPresent()) {
      final UseCaseConfig useCase = optUseCase.get();
      final String activeConfig = useCase.getActiveConfig();
      final Optional<Config> optConfig =
          useCase.getConfigs().stream().filter(c -> activeConfig.equals(c.getName())).findFirst();
      if (optConfig.isPresent()) {
        final Config config = optConfig.get();
        final UsecaseResult usecaseResult = isValid ? SUCCESS : FAILURE;
        final List<Adapter> adapters = config.getAdapters().stream()
            .filter(c -> c.getAdapterType().equals(Adapter.AdapterType.OUTBOUND)
                && c.getUsecaseResult().equals(usecaseResult))
            .collect(Collectors.toList());
        for (final Adapter adapter : adapters) {
          log.trace("Notifying outbound adapter: {}", adapter);
          final Transformer transformer = getTransformer(adapter.getTransformationType());
          final TransportType trasnportType = adapter.getTrasnportType();
          switch (trasnportType) {
            case JMS:
              notifyJms(event, domainEntity, adapter, transformer);
              break;
            case REST:
              notifyRest(event, domainEntity, adapter, transformer);
              break;
            default:
              log.error("Invalid adapter transport type: {} for adapter: {}", trasnportType,
                  adapter);
          }
        }
      }
    }
  }

  private Transformer getTransformer(final TransformationType transformationType) {
    Transformer transformer;
    switch (transformationType) {
      case DOMAIN_EVENT_FTL_TRANSFORMER:
        transformer = domainToDomainEventJsonFtlTransformer;
        break;
      case DOMAIN_EVENT_VELOCITY_TRANSFORMER:
        transformer = domainToDomainEventJsonVelocityTransformer;
        break;
      case FTL_TEMPLATE_TRANSFORMER:
        transformer = domainToFtlTemplateTransformer;
        break;
      case VELOCITY_TEMPLATE_TRANSFORMER:
        transformer = domainToVelocityTemplateTransformer;
        break;
      default:
        transformer = domainToStringTransformer;
        break;
    }
    return transformer;
  }

  private void notifyJms(final Event event, final Object domainEntity, final Adapter adapter,
      final Transformer transformer) throws Exception {
    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put(Transformer.DOMAIN_ENTITY, domainEntity);
    final String payload = transformer.transform(event, paramsMap, adapter.getTemplate());
    posterOutboundJmsPort.post(adapter.getBrokerId(), adapter.getQueueName(), payload);
  }

  private void notifyRest(final Event event, final Object domainEntity, final Adapter adapter,
      final Transformer transformer) throws Exception {
    final Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put(Transformer.DOMAIN_ENTITY, domainEntity);
    paramsMap.putAll(getCustomParams(event, adapter.getTemplateCustomizer()));
    final String requestHeader =
        transformer.transform(event, paramsMap, adapter.getRequestHeaderTemplate());
    final Map<String, String> httpHeadersMap =
        JsonUtils.unmarshal(Optional.ofNullable(requestHeader), Map.class.getCanonicalName());
    final String requestBody =
        transformer.transform(event, paramsMap, adapter.getRequestBodyTemplate());
    final String uri = transformer.transform(event, paramsMap, adapter.getUriTemplate());
    final String url =
        adapter.getProtocol() + "://" + adapter.getHostName() + ":" + adapter.getPort() + uri;
    posterOutboundWebPort.post(url, adapter.getMethodType(), requestBody, httpHeadersMap);
  }

  private Map<String, Object> getCustomParams(Event event, String customizerClassName) {
    Map<String, Object> params = MapUtils.EMPTY_MAP;
    StringBuilder builder = new StringBuilder();
    builder.append("com.scoperetail.fusion.orchestrator.config.plugins.").append(event.name())
        .append(".").append(customizerClassName);
    try {
      Class customizerClazz = Class.forName(builder.toString());
      Method method = customizerClazz.getDeclaredMethod("getParamsMap", new Class[0]);
      params = (Map<String, Object>) method.invoke(null, new Object[0]);
    } catch (Exception e) {
      log.error(
          "Skipping customization. Unable to load configured customizer for event: {} customizer: {}",
          event, builder.toString());
    }
    return params;
  }
}
