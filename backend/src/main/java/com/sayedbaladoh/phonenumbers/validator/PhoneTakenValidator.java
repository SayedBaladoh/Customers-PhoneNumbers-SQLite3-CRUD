package com.sayedbaladoh.phonenumbers.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.sayedbaladoh.phonenumbers.service.CustomerService;

public class PhoneTakenValidator implements ConstraintValidator<PhoneTaken, String> {

	@Autowired
	CustomerService customerService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctx) {
		if (customerService != null)
			return !customerService.existsByPhone(value);

		return true;
	}

}
