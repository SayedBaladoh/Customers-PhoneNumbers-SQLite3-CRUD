package com.sayedbaladoh.phonenumbers.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.model.CustomerCountryStatistics;
import com.sayedbaladoh.phonenumbers.model.IdentityAvailability;
import com.sayedbaladoh.phonenumbers.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Customer REST Controller provides REST APIs for <code>Customer<code> CRUD
 * operations
 * 
 * @author Sayed Baladoh
 *
 */
@Api(
		value = "Customer Controller",
		description = "REST APIs for Customer's Operations")
@RequestMapping("/api/customers")
@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	CustomerService customerService;

	/**
	 * Add a new customer.
	 * 
	 * @param customer
	 *            A valid customer details.
	 * @return Customer details with Id.
	 */
	@ApiOperation(
			value = "Add a new customer.",
			response = Customer.class)
	@PostMapping
	public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) {
		Customer savedCustomer = customerService.save(customer);
		return ResponseEntity.status(201).body(savedCustomer);
	}

	/**
	 * Update an existing customer.
	 * 
	 * @param customerId
	 *            The customer Id.
	 * @param customer
	 *            A valid customer details.
	 * @return Customer with updated details.
	 */
	@ApiOperation(
			value = "Update an existing customer.",
			response = Customer.class)
	@PutMapping("/{customerId}")
	public Customer editCustomer(@ApiParam("The customer Id.") @PathVariable Long customerId,
			@RequestBody Customer customerRequest) {
		return customerService.get(customerId).map(customer -> {
			if (customerRequest.getCountryCode() != null)
				customer.setCountryCode(customerRequest.getCountryCode());
			if (customerRequest.getEmail() != null)
				customer.setEmail(customerRequest.getEmail());
			if (customerRequest.getGender() != null)
				customer.setGender(customerRequest.getGender());
			if (customerRequest.getName() != null)
				customer.setName(customerRequest.getName());
			if (customerRequest.getPhone() != null)
				customer.setPhone(customerRequest.getPhone());
			return customerService.save(customer);
		}).orElseThrow(() -> new ResourceNotFoundException("Customer", "Id", customerId));
	}

	/**
	 * Delete an existing customer.
	 * 
	 * @param customerId
	 *            The customer Id.
	 * @return
	 */
	@ApiOperation(
			value = "Delete an existing customer by Id.")
	@DeleteMapping("/{customerId}")
	public ResponseEntity<?> deleteCustomer(@ApiParam("The customer Id.") @PathVariable Long customerId) {
		return customerService.get(customerId).map(customer -> {
			customerService.delete(customerId);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Customer", "Id", customerId));
	}

	/**
	 * Get a page contains a list of available customers.
	 * 
	 * @param pageable
	 *            Query page and sort options.
	 * @return A page contains list of available customers.
	 */
	@ApiOperation(
			value = "View a page contains a list of available customers.",
			response = Page.class)
	@ApiPageable
	@GetMapping()
	public Page<Customer> getCustomers(Pageable pageable) {
		return customerService.getAll(pageable);
	}

	/**
	 * Get a page contains a list of available customers by name contains value.
	 * 
	 * @param name
	 *            The customer name.
	 * @param pageable
	 *            Query page and sort options.
	 * @return A page contains list of available customers by customer name.
	 */
	@ApiOperation(
			value = "Get page of customers by customer name.",
			response = Page.class)
	@ApiPageable
	@GetMapping("/name/{name}")
	public Page<Customer> getCustomersByName(@ApiParam("The customer name.") @PathVariable(
			value = "name") String name, Pageable pageable) {
		return customerService.getAllByNameContains(name, pageable);
	}

	/**
	 * Get a page contains a list of available customers by country code.
	 * 
	 * @param countryCode
	 *            The country code.
	 * @param pageable
	 *            Query page and sort options.
	 * @return A page contains list of available customers by country code.
	 */
	@ApiOperation(
			value = "Get page of customers by country code.",
			response = Page.class)
	@ApiPageable
	@GetMapping("/country_code/{code}")
	public Page<Customer> getCustomersByCountryCode(@ApiParam("The customer country code.") @PathVariable(
			value = "code") String countryCode, Pageable pageable) {
		return customerService.getAllByCountryCode(countryCode, pageable);
	}

	/**
	 * Get customers count by country code.
	 * 
	 * @return A list of customers count by country code.
	 */
	@ApiOperation(
			value = "Get customers count by country code.",
			response = List.class)
	@GetMapping("/count/country_code")
	public List<CustomerCountryStatistics> getCountByCountryCode() {
		return customerService.getCountByCountryCode();
	}

	/**
	 * Get the customer profile by Id.
	 * 
	 * @param id
	 *            The customer Id.
	 * @return The customer profile details.
	 */
	@ApiOperation(
			value = "Get the customer profile by Id.",
			response = Customer.class)
	@GetMapping("/{id}")
	public Customer getCustomerProfile(@ApiParam("The customer Id.") @PathVariable(
			value = "id") Long id) {
		return customerService.get(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "Id",
						id));
	}

	/**
	 * Get the customer profile by phone number.
	 * 
	 * @param phone
	 * @return
	 */
	@ApiOperation(
			value = "Get the customer profile by phone number.",
			response = Customer.class)
	@GetMapping("/phone/{number}")
	public Customer getCustomerProfile(@ApiParam("The customer phone number.") @PathVariable(
			value = "number") String phone) {
		Customer customer = customerService.getByPhone(phone)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "phone",
						phone));

		return customer;
	}

	/**
	 * Check if a phone number is available for a new customer.
	 * 
	 * @param phone
	 *            The customer phone number.
	 * @return
	 */
	@ApiOperation(
			value = "Check if a phone number is available for a new customer.",
			response = IdentityAvailability.class)
	@GetMapping("/availabile/phone/{number}")
	public IdentityAvailability checkPhoneNumberAvailability(@ApiParam("The customer phone number.") @PathVariable(
			value = "number") String phone) {
		Boolean isAvailable = !customerService.existsByPhone(phone);
		return new IdentityAvailability(isAvailable);
	}

	/**
	 * Check if an email is available for a new customer.
	 * 
	 * @param email
	 *            The customer email.
	 * @return
	 */
	@ApiOperation(
			value = "Check if an email is available for a new customer.",
			response = IdentityAvailability.class)
	@GetMapping("/availabile/email/{email}")
	public IdentityAvailability checkEmailAvailability(@ApiParam("The customer email.") @PathVariable(
			value = "email") String email) {
		Boolean isAvailable = !customerService.existsByEmail(email);
		return new IdentityAvailability(isAvailable);
	}

}
