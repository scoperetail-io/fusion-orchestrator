package com.scoperetail.fusion.orchestrator.adapter.out.messaging.jms;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterSender;
import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import com.scoperetail.fusion.messaging.config.Adapter;
import com.scoperetail.fusion.messaging.config.Config;
import com.scoperetail.fusion.messaging.config.FusionConfig;
import com.scoperetail.fusion.messaging.config.UseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.events.DomainEvent;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.common.annotation.MessagingAdapter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@MessagingAdapter
@AllArgsConstructor
@Slf4j
public class PosterOutboundJMSAdapter implements PosterOutboundPort {

	private MessageRouterSender messageSender;

	private FusionConfig fusionConfig;

	@Override
	public boolean post(final DomainEvent domainEvent, UsecaseResult usecaseResult) throws IOException {
		boolean result = false;
		Optional<UseCase> optUseCase = fusionConfig.getUsecases().stream()
				.filter(u -> u.getName().equals(domainEvent.getEvent().name())).findFirst();
		if (optUseCase.isPresent()) {
			UseCase useCase = optUseCase.get();
			String activeConfig = useCase.getActiveConfig();
			Optional<Config> optConfig = useCase.getConfigs().stream().filter(c -> activeConfig.equals(c.getName()))
					.findFirst();
			if (optConfig.isPresent()) {
				List<Adapter> adapters = optConfig.get().getAdapters().stream()
						.filter(c -> c.getAdapterType().equals(Adapter.AdapterType.OUTBOUND)
								&& c.getType().equals(Adapter.Type.JMS) && c.getUsecaseResult().equals(usecaseResult))
						.collect(Collectors.toList());
				String payload = JsonUtils.marshal(Optional.ofNullable(domainEvent));
				adapters.forEach(adapter -> {
					messageSender.send(adapter.getBrokerId(), adapter.getQueueName(), payload);
				});
				result = true;
			}
		}
		return result;
	}

}
