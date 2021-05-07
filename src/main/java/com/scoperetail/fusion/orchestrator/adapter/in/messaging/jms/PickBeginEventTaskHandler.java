package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import javax.xml.validation.Schema;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.pick.begin.PickingOrderBeginEventMessage;
import com.scoperetail.fusion.orchestrator.application.port.in.query.PickBeginUseCase;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PickBeginEventTaskHandler extends AbstractMessageListener {

	PickBeginUseCase pickBeginUseCase;

	public PickBeginEventTaskHandler(MessageRouterReceiver messageRouterReceiver, Schema pickBeginXmlSchema,
			PickBeginUseCase pickBeginUseCase) {
		super("mcsBroker", "MCS.PICK.BEGIN.OUT", MessageType.XML, pickBeginXmlSchema, messageRouterReceiver);
		this.pickBeginUseCase = pickBeginUseCase;
	}

	@Override
	protected boolean handleMessage(Object event, boolean isValid) {
		return pickBeginUseCase.beginPick(event, isValid);

	}

	@Override
	protected Class getClazz() {
		return PickingOrderBeginEventMessage.class;
	}
}
