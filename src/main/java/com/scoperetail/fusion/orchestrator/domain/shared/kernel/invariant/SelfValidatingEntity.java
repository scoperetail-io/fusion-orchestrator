package com.scoperetail.fusion.orchestrator.domain.shared.kernel.invariant;

//import java.util.Set;

//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;

public abstract class SelfValidatingEntity<T> {

	//private Validator validator;

//	public SelfValidatingEntity() {
//		init();
//	}
//
//	public void init() {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		validator = factory.getValidator();
//	}
//
//	/** Evaluates all Bean Validations on the attributes of this instance. */
//	@SuppressWarnings("unchecked")
//	public void validateSelf() {
//		if(this.validator == null) {
//			init();
//		}
//		
//		Set<ConstraintViolation<T>> violations = validator.validate((T) this);
//		if (!violations.isEmpty()) {
//			throw new ConstraintViolationException(violations);
//		}
//	}
}
