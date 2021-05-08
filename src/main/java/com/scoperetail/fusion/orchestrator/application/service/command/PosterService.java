package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.FAILURE;
import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.SUCCESS;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import com.scoperetail.fusion.messaging.config.Adapter;
import com.scoperetail.fusion.messaging.config.Broker;
import com.scoperetail.fusion.messaging.config.Config;
import com.scoperetail.fusion.messaging.config.FusionConfig;
import com.scoperetail.fusion.messaging.config.UseCaseConfig;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToDomainEventJsonTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToHttpTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToXmlTransformer;
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

	private DomainToXmlTransformer domainToXmlTransformer;

	private DomainToHttpTransformer domainToHttpTransformer;

	private FusionConfig fusionConfig;

	private Map<String, Broker> brokersByBrokerIdMap;

	@PostConstruct
	private void intialize() {
		brokersByBrokerIdMap = fusionConfig.getBrokers().stream()
				.collect(Collectors.toMap(Broker::getBrokerId, b -> b));
	}

	@Override
	public boolean post(final Event event, final Object domainEntity, final boolean isValid) {
		boolean result;
		try {
			result = handleEvent(event, domainEntity, isValid);
		} catch (final Exception e) {
			result = false;
			log.error("PosterService:: {}", e);
		}
		return result;
	}

	private boolean handleEvent(final Event event, final Object domainEntity, final boolean isValid) throws Exception {
		final String eventName = event.name();
		boolean result = false;
		final Optional<UseCaseConfig> optUseCase = fusionConfig.getUsecases().stream()
				.filter(u -> u.getName().equals(eventName)).findFirst();
		if (optUseCase.isPresent()) {
			final UseCaseConfig useCase = optUseCase.get();
			final String activeConfig = useCase.getActiveConfig();
			final Optional<Config> optConfig = useCase.getConfigs().stream()
					.filter(c -> activeConfig.equals(c.getName())).findFirst();
			if (optConfig.isPresent()) {
				result = notifyJms(event, domainEntity, result, optConfig, isValid);
				result = notifyRest(event, domainEntity, result, optConfig, isValid);

			}
		}
		return result;
	}

	private boolean notifyRest(final Event event, final Object domainEntity, boolean result,
			final Optional<Config> optConfig, final boolean isValid) throws Exception, IOException {
		final UsecaseResult usecaseResult = isValid ? SUCCESS : FAILURE;
		final List<Adapter> adapters = optConfig.get().getAdapters().stream()
				.filter(c -> c.getAdapterType().equals(Adapter.AdapterType.OUTBOUND)
						&& c.getTrasnportType().equals(Adapter.TransportType.REST)
						&& c.getUsecaseResult().equals(usecaseResult))
				.collect(Collectors.toList());

		for (final Adapter adapter : adapters) {
			final RestTemplate restTemplate = new RestTemplate();
			final String uri = domainToHttpTransformer.transform(event, domainEntity, adapter.getUriTemplate());
			final String url = adapter.getProtocol() + "://" + adapter.getHostName() + ":" + adapter.getPort() + uri;

			final String requestHeader = domainToHttpTransformer.transform(event, domainEntity,
					adapter.getRequestHeaderTemplate());
			final Map<String, String> httpHeadersMap = JsonUtils.unmarshal(Optional.ofNullable(requestHeader),
					Map.class.getCanonicalName());
			final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeadersMap.entrySet().forEach(mapEntry -> {
				httpHeaders.add(mapEntry.getKey(), mapEntry.getValue());
			});

			final String requestBody = domainToHttpTransformer.transform(event, domainEntity,
					adapter.getRequestBodyTemplate());

			final HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, httpHeaders);
			final ResponseEntity<String> exchange = restTemplate.exchange(url,
					HttpMethod.valueOf(adapter.getMethodType()), httpEntity, String.class);
			System.out.println("REST RESPONSE IS " + exchange);
			result = true;
		}
		return result;
	}

	private boolean notifyJms(final Event event, final Object domainEntity, boolean result,
			final Optional<Config> optConfig, final boolean isValid) throws Exception, IOException {
		final UsecaseResult usecaseResult = isValid ? SUCCESS : FAILURE;
		final List<Adapter> adapters = optConfig.get().getAdapters().stream()
				.filter(c -> c.getAdapterType().equals(Adapter.AdapterType.OUTBOUND)
						&& c.getTrasnportType().equals(Adapter.TransportType.JMS)
						&& c.getUsecaseResult().equals(usecaseResult))
				.collect(Collectors.toList());

		for (final Adapter adapter : adapters) {
			final Broker broker = brokersByBrokerIdMap.get(adapter.getBrokerId());
			String payload;
			if (Broker.Owner.SCOPE.equals(broker.getOwner())) {
				payload = domainToDomainEventJsonTransformer.transform(event, domainEntity);
			} else {
				payload = domainToXmlTransformer.transform(event, domainEntity);
			}
			posterOutboundPort.post(adapter.getBrokerId(), adapter.getQueueName(), payload);
			result = true;
		}
		return result;
	}

}
