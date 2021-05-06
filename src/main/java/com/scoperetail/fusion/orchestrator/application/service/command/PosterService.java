package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.SUCCESS;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;

import com.scoperetail.fusion.messaging.config.Adapter;
import com.scoperetail.fusion.messaging.config.Broker;
import com.scoperetail.fusion.messaging.config.Config;
import com.scoperetail.fusion.messaging.config.FusionConfig;
import com.scoperetail.fusion.messaging.config.UseCaseConfig;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToDomainEventJsonTransformer;
import com.scoperetail.fusion.orchestrator.application.service.transform.impl.DomainToXmlTransformer;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
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

	private FusionConfig fusionConfig;

	private Map<String, Broker> brokersByBrokerIdMap;

	@PostConstruct
	private void intialize() {
		brokersByBrokerIdMap = fusionConfig.getBrokers().stream()
				.collect(Collectors.toMap(Broker::getBrokerId, b -> b));
	}

	@Override
	public ModelApiResponse post(final Event event, final Object domainEntity) {
		boolean result;
		try {
			result = handleEvent(event, domainEntity);
		} catch (Exception e) {
			result = false;
			log.error("PosterService {}", e);
		}
		return buildResult(result);
	}

	private boolean handleEvent(final Event event, final Object domainEntity) throws Exception {
		String eventName = event.name();
		boolean result = false;
		Optional<UseCaseConfig> optUseCase = fusionConfig.getUsecases().stream()
				.filter(u -> u.getName().equals(eventName)).findFirst();
		if (optUseCase.isPresent()) {
			UseCaseConfig useCase = optUseCase.get();
			String activeConfig = useCase.getActiveConfig();
			Optional<Config> optConfig = useCase.getConfigs().stream().filter(c -> activeConfig.equals(c.getName()))
					.findFirst();
			if (optConfig.isPresent()) {
				List<Adapter> adapters = optConfig.get().getAdapters().stream()
						.filter(c -> c.getAdapterType().equals(Adapter.AdapterType.OUTBOUND)
								&& c.getType().equals(Adapter.Type.JMS) && c.getUsecaseResult().equals(SUCCESS))
						.collect(Collectors.toList());

				for (Adapter adapter : adapters) {
					Broker broker = brokersByBrokerIdMap.get(adapter.getBrokerId());
					String payload;
					if (Broker.Owner.SCOPE.equals(broker.getOwner())) {
						payload = domainToDomainEventJsonTransformer.transform(event,domainEntity);
					} else {
						payload = domainToXmlTransformer.transform(event,domainEntity);
					}
					posterOutboundPort.post(adapter.getBrokerId(), adapter.getQueueName(), payload);
					result = true;
				}
			}
		}
		return result;
	}

	private ModelApiResponse buildResult(boolean result) {
		HttpStatus httpStatus = result ? ACCEPTED : INTERNAL_SERVER_ERROR;
		ModelApiResponse response = new ModelApiResponse();
		response.setCode(httpStatus.value());
		response.setMessage(httpStatus.getReasonPhrase());
		response.setType(httpStatus.name());
		return response;
	}

}
