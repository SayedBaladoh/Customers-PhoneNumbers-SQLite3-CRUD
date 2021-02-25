package com.sayedbaladoh.phonenumbers.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.sayedbaladoh.phonenumbers.service.CustomerService;

public class EmailTakenValidator implements ConstraintValidator<EmailTaken, String> {

	@Autowired
	CustomerService customerService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctx) {
		if (value != null && customerService != null)
			return !customerService.existsByEmail(value);

		return true;
	}

}
