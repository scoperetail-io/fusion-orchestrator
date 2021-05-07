package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import static java.util.Optional.ofNullable;

import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.util.JaxbUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageListener implements MessageListener<String> {

	private MessageType messageType;
	Schema schema;

	protected enum MessageType {
		XML, JSON
	}

	public AbstractMessageListener(String broker, String queue, MessageType messageType, Schema schema,
			MessageRouterReceiver messageRouterReceiver) {
		this.messageType = messageType;
		this.schema = schema;
		messageRouterReceiver.registerListener(broker, queue, this);
	}

	public TaskResult doTask(final String message) {
		log.info("The message :: {}", message);
		boolean isValid = validate(message);
		Object event = unmarshal(message);
		TaskResult taskResult = handleMessage(event, isValid) ? TaskResult.SUCCESS : TaskResult.FAILURE;
		return isValid ? taskResult : TaskResult.DISCARD;
	}

	private Object unmarshal(final String message) {
		Object object = message;
		try {
			if (messageType == MessageType.XML) {
				object = JaxbUtil.unmarshal(ofNullable(message), ofNullable(schema), getClazz());
			}
		} catch (SAXException e) {
			log.error("{}", e);
		} catch (JAXBException e) {
			log.error("{}", e);
		}
		return object;
	}

	protected boolean validate(String message) {
		boolean result = true;
		if (messageType.equals(MessageType.XML)) {
			result = JaxbUtil.isValidMessage(message, schema);
		}
		return result;
	}

	protected abstract boolean handleMessage(Object event, boolean isValid);

	protected Class getClazz() {
		return String.class;
	}

}
