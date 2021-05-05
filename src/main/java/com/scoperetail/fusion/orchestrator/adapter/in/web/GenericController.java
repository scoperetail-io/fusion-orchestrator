package com.scoperetail.fusion.orchestrator.adapter.in.web;

import javax.servlet.http.HttpServletRequest;
//import javax.validation.constraints.NotBlank;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
//import com.scoperetail.fusion.orchestrator.domain.entities.old.Customer;

public abstract class GenericController {
	private final PosterUseCase posterUseCase;

	private final Environment env;
	
	public GenericController(final PosterUseCase posterUseCase, final Environment env){
		this.posterUseCase= posterUseCase;
		this.env= env;
	}

//	public ResponseEntity<Customer> handle(@RequestBody @NotBlank String json, HttpServletRequest httpServletRequest) {
//
//		String requestContext = httpServletRequest.getMethod() + "_" + httpServletRequest.getRequestURI();
//		try {
//
//			Gson g = new Gson();
//			Object domainEntity = g.fromJson(json,
//					TypeToken.getParameterized(Class.forName(env.getProperty(requestContext))).getType());
//
//			final Object obj = posterUseCase.post(domainEntity);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		return ResponseEntity.ok().build();
//	}

}
