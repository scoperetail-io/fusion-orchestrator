package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.FAILURE;
import static com.scoperetail.fusion.messaging.application.port.in.UsecaseResult.SUCCESS;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.common.HashUtil;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.events.DomainEvent;
import com.scoperetail.fusion.orchestrator.domain.events.Event;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.common.annotation.UseCase;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.helper.DomainHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
class PosterService implements PosterUseCase {

	private final PosterOutboundPort posterOutboundPort;

	private final DomainHelper domainHelper;

	@Override
	public ModelApiResponse post(final Object entity) {
		boolean result = false;
		Event eventName = Event.valueOf(entity.getClass().getSimpleName());
		String payload = eventName.toString();
		String keyHash = null;
		Map<String, String> keyMap = null;
		try {
			log.trace("Processing message {}", eventName);
			payload = JsonUtils.marshal(Optional.ofNullable(entity));
			log.trace("Marshalled message {}", eventName);
			String keyText = domainHelper.getHashKey(entity);
			log.trace("Hash key text generated {}", eventName);
			keyMap = JsonUtils.unmarshal(Optional.of(keyText), Map.class.getCanonicalName());
			log.trace("Hash key text unmarshaled {}", eventName);
			keyHash = HashUtil.getHash(keyText);
			log.trace("Created hash of hash key {}", eventName);
			DomainEvent domainEvent = DomainEvent.builder().eventId(keyHash).event(eventName).keyMap(keyMap)
					.payload(payload).build();
			log.trace("Posting domain event {} with result {}", eventName, SUCCESS);
			result = posterOutboundPort.post(domainEvent, SUCCESS);
			log.trace("Successfully posted domain event {}", eventName);
		} catch (Exception e) {
			log.error("Unable to execute the use case: {} exception: {}", eventName, e);
			DomainEvent domainEvent = DomainEvent.builder().eventId(keyHash).event(eventName).keyMap(keyMap)
					.payload(payload).build();
			try {
				log.trace("Posting domain event {} with result {}", FAILURE);
				posterOutboundPort.post(domainEvent, FAILURE);
				log.trace("Successfully posted domain event {}", eventName);
			} catch (IOException ioException) {
				log.error("Unable to execute the use case: {} failed to send to error queue exception is: {}",
						eventName, ioException);
			}
		}
		return buildResult(result);
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
