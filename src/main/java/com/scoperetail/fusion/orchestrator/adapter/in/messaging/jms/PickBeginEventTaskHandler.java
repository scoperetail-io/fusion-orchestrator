package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PickBeginEventTaskHandler implements MessageListener<String> {

	MessageRouterReceiver messageRouterReceiver;

	@PostConstruct
	private void initialize() {
		messageRouterReceiver.registerListener("mcsBroker", "MCS.OUT", this);
	}

	@Override
	public TaskResult doTask(String message) {
		System.out.println(PickBeginEventTaskHandler.class.getCanonicalName());
		return TaskResult.SUCCESS;
	}

}
