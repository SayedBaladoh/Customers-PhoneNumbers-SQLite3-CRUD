package com.sayedbaladoh.phonenumbers.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.sayedbaladoh.phonenumbers.service.CountryService;

/**
 * Validator of country dial code.
 * 
 * Check if a value is a valid country phone code
 * 
 * @author Sayed Baladoh
 *
 */
public class CountryDialCodeValidator implements ConstraintValidator<CountryDialCode, String> {

	@Autowired
	CountryService countryService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctx) {

		if (countryService != null) {
			if (value.matches("^(\\\\(\\\\d{1,6}\\\\))[- .]?(\\\\d{3}[- .]?){2}\\\\d{1,4}$")) {
				int index = value.indexOf(")");
				String isd = value.substring(1, index);
				// System.out.println("Phone: " + value + ", ISD: " + isd);
				return countryService.existsByIsd(isd);
			}
		}

		return true;
	}

}
