package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.FAILURE;
import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.SUCCESS;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import com.scoperetail.fusion.messaging.config.Adapter;
import com.scoperetail.fusion.messaging.config.Adapter.TransformationType;
import com.scoperetail.fusion.messaging.config.Adapter.TransportType;
import com.scoperetail.fusion.messaging.config.Config;
import com.scoperetail.fusion.messaging.config.FusionConfig;
import com.scoperetail.fusion.messaging.config.UseCaseConfig;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.application.service.transform.Transformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToDomainEventJsonTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToStringTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToTemplateTransformer;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@AllArgsConstructor
@Slf4j
class PosterService implements PosterUseCase {

	private final PosterOutboundPort posterOutboundPort;

	private DomainToDomainEventJsonTransformer domainToDomainEventJsonTransformer;

	private DomainToTemplateTransformer domainToTemplateTransformer;

	private DomainToStringTransformer domainToStringTransformer;

	private FusionConfig fusionConfig;

	@Override
	public void post(final Event event, final Object domainEntity, final boolean isValid) throws Exception {
		handleEvent(event, domainEntity, isValid);
	}

	private void handleEvent(final Event event, final Object domainEntity, final boolean isValid) throws Exception {
		final Optional<UseCaseConfig> optUseCase = fusionConfig.getUsecases().stream()
				.filter(u -> u.getName().equals(event.name())).findFirst();
		if (optUseCase.isPresent()) {
			final UseCaseConfig useCase = optUseCase.get();
			final String activeConfig = useCase.getActiveConfig();
			final Optional<Config> optConfig = useCase.getConfigs().stream()
					.filter(c -> activeConfig.equals(c.getName())).findFirst();
			if (optConfig.isPresent()) {
				final Config config = optConfig.get();
				final UsecaseResult usecaseResult = isValid ? SUCCESS : FAILURE;
				final List<Adapter> adapters = config.getAdapters().stream()
						.filter(c -> c.getAdapterType().equals(Adapter.AdapterType.OUTBOUND)
								&& c.getUsecaseResult().equals(usecaseResult))
						.collect(Collectors.toList());
				for (final Adapter adapter : adapters) {
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
						log.error("Invalid adapter transport type: {} for adapter: {}", trasnportType, adapter);
					}
				}
			}
		}
	}

	private Transformer getTransformer(final TransformationType transformationType) {
		Transformer transformer;
		switch (transformationType) {
		case DOMAIN_EVENT_TRANSFORMER:
			transformer = domainToDomainEventJsonTransformer;
			break;
		case TEMPLATE_TRANSFORMER:
			transformer = domainToTemplateTransformer;
			break;
		default:
			transformer = domainToStringTransformer;
			break;
		}
		return transformer;
	}

	private void notifyJms(final Event event, final Object domainEntity, final Adapter adapter,
			final Transformer transformer) throws Exception {
		final String payload = transformer.transform(event, domainEntity, adapter.getTemplate());
		posterOutboundPort.post(adapter.getBrokerId(), adapter.getQueueName(), payload);
	}

	private void notifyRest(final Event event, final Object domainEntity, final Adapter adapter,
			final Transformer transformer) throws Exception {
		final RestTemplate restTemplate = new RestTemplate();
		final String requestHeader = transformer.transform(event, domainEntity, adapter.getRequestHeaderTemplate());
		final Map<String, String> httpHeadersMap = JsonUtils.unmarshal(Optional.ofNullable(requestHeader),
				Map.class.getCanonicalName());
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeadersMap.entrySet().forEach(mapEntry -> {
			httpHeaders.add(mapEntry.getKey(), mapEntry.getValue());
		});

		final String requestBody = transformer.transform(event, domainEntity, adapter.getRequestBodyTemplate());

		final HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, httpHeaders);

		final String uri = transformer.transform(event, domainEntity, adapter.getUriTemplate());
		final String url = adapter.getProtocol() + "://" + adapter.getHostName() + ":" + adapter.getPort() + uri;
		final ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.valueOf(adapter.getMethodType()),
				httpEntity, String.class);
		log.trace("Response received for URL: {} Response: {}", url, exchange);
	}
}
