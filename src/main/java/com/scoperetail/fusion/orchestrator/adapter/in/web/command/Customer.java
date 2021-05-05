package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Customer {

	@NotNull
	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
