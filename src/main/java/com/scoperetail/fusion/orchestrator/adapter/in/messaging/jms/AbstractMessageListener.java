package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import static com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult.FAILURE;
import static com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult.SUCCESS;
import static java.util.Optional.ofNullable;

import javax.xml.validation.Schema;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.util.JaxbUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageListener implements MessageListener<String> {

	private final MessageType messageType;
	Schema schema;

	protected enum MessageType {
		XML, JSON
	}

	protected AbstractMessageListener(final String broker, final String queue, final MessageType messageType,
			final Schema schema, final MessageRouterReceiver messageRouterReceiver) {
		this.messageType = messageType;
		this.schema = schema;
		messageRouterReceiver.registerListener(broker, queue, this);
	}

	@Override
	public TaskResult doTask(final String message) throws Exception {
		log.info("The message :: {}", message);
		Object object = message;
		boolean isValid = validate(message);
		if (isValid) {
			try {
				if (schema != null) {
					object = JaxbUtil.unmarshal(ofNullable(message), ofNullable(schema), getClazz());
				}
			} catch (final Exception t) {
				log.error("Unable to unmarshal incoming message: {} Exception occured: {}", message, t);
				isValid = false;
			}
		}
		handleMessage(object, isValid);
		return isValid ? SUCCESS : FAILURE;
	}

	protected boolean validate(final String message) {
		boolean result = true;
		if (messageType.equals(MessageType.XML)) {
			result = JaxbUtil.isValidMessage(message, schema);
		}
		return result;
	}

	protected abstract void handleMessage(Object event, boolean isValid) throws Exception;

	protected Class getClazz() {
		return String.class;
	}

}
