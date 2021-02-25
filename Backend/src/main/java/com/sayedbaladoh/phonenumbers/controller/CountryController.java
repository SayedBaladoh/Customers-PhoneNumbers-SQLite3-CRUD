package com.sayedbaladoh.phonenumbers.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sayedbaladoh.phonenumbers.errorhandler.ResourceNotFoundException;
import com.sayedbaladoh.phonenumbers.model.ApiPageable;
import com.sayedbaladoh.phonenumbers.model.Country;
import com.sayedbaladoh.phonenumbers.service.CountryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Country REST Controller provides REST APIs for <code>Country<code> CRUD
 * operations
 * 
 * @author Sayed Baladoh
 *
 */
@Api(
		value = "Country Controller",
		description = "REST APIs for Country Operations")
@RequestMapping("/api/countries")
@RestController
public class CountryController {

	@Autowired
	CountryService countryService;

	/**
	 * Add a new country.
	 * 
	 * @param country
	 *            A valid country details.
	 * @return Country details with Id.
	 */
	@ApiOperation(
			value = "Add a new country.",
			response = Country.class)
	@PostMapping
	public ResponseEntity<?> addCountry(@Valid @RequestBody Country country) {
		Country savedCountry = countryService.save(country);
		return ResponseEntity.status(201).body(savedCountry);
	}

	/**
	 * Update an existing country.
	 * 
	 * @param countryId
	 *            The country Id.
	 * @param country
	 *            A valid country details.
	 * @return Country with updated details.
	 */
	@ApiOperation(
			value = "Update an existing country.",
			response = Country.class)
	@PutMapping("/{countryId}")
	public Country editCountry(@ApiParam("The country Id.") @PathVariable Long countryId,
			@RequestBody Country countryRequest) {
		return countryService.get(countryId).map(country -> {
			if (countryRequest.getName() != null)
				country.setName(countryRequest.getName());
			if (countryRequest.getAlpha2Code() != null)
				country.setAlpha2Code(countryRequest.getAlpha2Code());
			if (countryRequest.getAlpha3Code() != null)
				country.setAlpha3Code(countryRequest.getAlpha3Code());
			if (countryRequest.getIsd() != null)
				country.setIsd(countryRequest.getIsd());
			return countryService.save(country);
		}).orElseThrow(() -> new ResourceNotFoundException("Country", "Id", countryId));
	}

	/**
	 * Delete an existing country.
	 * 
	 * @param countryId
	 *            The country Id.
	 * @return
	 */
	@ApiOperation(
			value = "Delete an existing country by Id.")
	@DeleteMapping("/{countryId}")
	public ResponseEntity<?> deleteCountry(@ApiParam("The country Id.") @PathVariable Long countryId) {
		return countryService.get(countryId).map(country -> {
			countryService.delete(countryId);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Country", "Id", countryId));
	}

	/**
	 * Get a page contains a list of available countries.
	 * 
	 * @param pageable
	 *            Query page and sort options.
	 * @return A page contains list of available countries.
	 */
	@ApiOperation(
			value = "View a page contains a list of available countries.",
			response = Page.class)
	@ApiPageable
	@GetMapping()
	public Page<Country> getCountrys(Pageable pageable) {
		return countryService.getAll(pageable);
	}

	/**
	 * Get the country profile by Id.
	 * 
	 * @param id
	 *            The country Id.
	 * @return The country profile details.
	 */
	@ApiOperation(
			value = "Get the country profile by Id.",
			response = Country.class)
	@GetMapping("/{id}")
	public Country getCountryProfile(@ApiParam("The country Id.") @PathVariable(
			value = "id") Long id) {
		return countryService.get(id)
				.orElseThrow(() -> new ResourceNotFoundException("Country", "Id",
						id));
	}

	/**
	 * Get the country profile by alpha 2 code.
	 * 
	 * @param code
	 *            The alpha 2 country code.
	 * @return
	 */
	@ApiOperation(
			value = "Get the country profile by alpha 2 code.",
			response = Country.class)
	@GetMapping("/alpha_2_code/{code}")
	public Country getCountryProfileByAlpha2Code(@ApiParam("The alpha 2 country code.") @PathVariable(
			value = "code") String alpha2Code) {
		Country country = countryService.getByAlpha2Code(alpha2Code)
				.orElseThrow(() -> new ResourceNotFoundException("Country", "alpha2Code",
						alpha2Code));
		return country;
	}

	/**
	 * Get the country profile by alpha 3 code.
	 * 
	 * @param code
	 *            The alpha 3 country code.
	 * @return
	 */
	@ApiOperation(
			value = "Get the country profile by alpha 3 code.",
			response = Country.class)
	@GetMapping("/alpha_3_code/{code}")
	public Country getCountryProfileByAlpha3Code(@ApiParam("The alpha 3 country code.") @PathVariable(
			value = "code") String alpha3Code) {
		Country country = countryService.getByAlpha3Code(alpha3Code)
				.orElseThrow(() -> new ResourceNotFoundException("Country", "alpha3Code",
						alpha3Code));
		return country;
	}

	/**
	 * Get the country profile by ISD international standard dialing code.
	 * 
	 * @param code
	 *            The international standard dialing code.
	 * @return
	 */
	@ApiOperation(
			value = "Get the country profile by ISD international standard dialing code.",
			response = Country.class)
	@GetMapping("/isd/{code}")
	public Country getCountryProfileByIsd(@ApiParam("The international standard dialing code.") @PathVariable(
			value = "code") String isd) {
		Country country = countryService.getByIsd(isd)
				.orElseThrow(() -> new ResourceNotFoundException("Country", "isd",
						isd));
		return country;
	}

}
