package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

//import javax.validation.constraints.NotBlank;
import javax.validation.Valid;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scoperetail.fusion.orchestrator.adapter.in.web.GenericController;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
//import com.scoperetail.fusion.orchestrator.domain.entities.old.Customer;


@RestController
@RequestMapping("/v1/customers")
class CustomerCommandController extends GenericController {

	public CustomerCommandController(final PosterUseCase posterUseCase, final Environment env) {
		super(posterUseCase, env);
	}

//	@PostMapping()
//	public ResponseEntity<Customer> handle(@RequestBody @NotBlank String json, HttpServletRequest httpServletRequest) {
//		return super.handle(json, httpServletRequest);
//	}
	
	@PostMapping()
	public ResponseEntity<Customer> handle(@RequestBody @Valid  Customer customer) {
		return ResponseEntity.ok(customer);
	}
}
