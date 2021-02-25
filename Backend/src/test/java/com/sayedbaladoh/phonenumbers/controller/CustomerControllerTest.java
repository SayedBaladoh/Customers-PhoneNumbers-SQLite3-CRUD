/**
 * 
 */
package com.sayedbaladoh.phonenumbers.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.service.CountryService;
import com.sayedbaladoh.phonenumbers.service.CustomerService;
import com.sayedbaladoh.phonenumbers.util.JsonUtil;

/**
 * Customer controller units' test
 * 
 * Test the Customer rest web services' unit tests
 * 
 * @author Sayed Baladoh
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		value = CustomerController.class)
class CustomerControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CustomerService customerService;

	@MockBean
	private CountryService countryService;

	/**
	 * Validate get all customers
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#getCustomers(org.springframework.data.domain.Pageable)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenCustomersList_whenGetCustomers_thenReturnJsonArray() throws Exception {
		// Data preparation
		Customer customer1 = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		Customer customer2 = getCustomer("Ahmed Mahmoud", "(20) 1235478912", "test2@test.com", "male", "EG");
		Customer customer3 = getCustomer("Sara Ahmed", "(20) 1235478921", "test3@test.com", "female", "EG");

		List<Customer> customers = Arrays.asList(customer1, customer2, customer3);
		Page<Customer> page = new PageImpl<Customer>(customers);

		given(customerService.getAll(any(Pageable.class)))
				.willReturn(page);

		// Verification
		mvc.perform(get("/api/customers")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(3)))
				.andExpect(jsonPath("$.content[0].name", is(customer1.getName())))
				.andExpect(jsonPath("$.content[1].name", is(customer2.getName())))
				.andExpect(jsonPath("$.content[2].name", is(customer3.getName())));

		verify(customerService, VerificationModeFactory.times(1)).getAll(any(Pageable.class));
		reset(customerService);
	}

	/**
	 * Verify valid Customer Id to get
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#getCustomerProfile(java.lang.Long)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenCustomer_whenGetCustomer_thenReturnCustomer() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.get(customer.getId()))
				.willReturn(Optional.of(customer));

		// Verification
		this.mvc.perform(get("/api/customers/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.id").value(customer.getId()))
				.andExpect(jsonPath("$.name").value(customer.getName()))
				.andExpect(jsonPath("$.email").value(customer.getEmail()))
				.andExpect(jsonPath("$.phone").value(customer.getPhone()))
				.andDo(print());

		verify(customerService, VerificationModeFactory.times(1)).get(customer.getId());
		reset(customerService);
	}

	/**
	 * Verify invalid Customer Id to get
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#getCustomerProfile(java.lang.Long)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenCustomer_whenGetInavlidCustomerId_thenReturn404() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.get(customer.getId()))
				.willReturn(Optional.of(customer));

		// Verification
		this.mvc.perform(get("/api/customers/55")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404))
				.andDo(print());

		reset(customerService);
	}

	/**
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#checkEmailAvailability(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCheckEmailAvailability() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.existsByEmail(customer.getEmail()))
				.willReturn(false);

		// Verification
		this.mvc.perform(get("/api/customers/availabile/email/" + customer.getEmail())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.available").exists())
				.andExpect(jsonPath("$.available").value(true))
				.andDo(print());

		reset(customerService);
	}

	/**
	 * Verify post a valid Customer
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#addCustomer(com.sayedbaladoh.phonenumbers.model.Customer)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void whenPostValidCustomer_thenAddCustomer() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		given(customerService.save(Mockito.anyObject()))
				.willReturn(customer);

		// Verification
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(customer)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.name", is(customer.getName())))
				.andExpect(jsonPath("$.email", is(customer.getEmail())));

		verify(customerService, VerificationModeFactory.times(1)).save(Mockito.anyObject());
		reset(customerService);
	}

	/**
	 * Verify update a valid Customer
	 *
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#editCustomer(java.lang.Long, com.sayedbaladoh.phonenumbers.model.Customer)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void whenPutValidCustomer_thenEditCustomer() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.get(customer.getId()))
				.willReturn(Optional.of(customer));

		given(customerService.save(Mockito.anyObject()))
				.willReturn(customer);

		// Verification
		mvc.perform(put("/api/customers/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(customer)))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name", is(customer.getName())))
				.andExpect(jsonPath("$.email").value(customer.getEmail()))
				.andDo(print());

		verify(customerService, VerificationModeFactory.times(1)).save(Mockito.anyObject());
		reset(customerService);
	}

	/**
	 * Verify update invalid Customer Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#editCustomer(java.lang.Long, com.sayedbaladoh.phonenumbers.model.Customer)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void whenPutInvalidCustomerId_thenNotEditCustomer() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.get(customer.getId()))
				.willReturn(Optional.of(customer));

		given(customerService.save(Mockito.anyObject()))
				.willReturn(customer);

		// Verification
		mvc.perform(put("/api/customers/55")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(customer)))
				.andExpect(status().is(404))
				.andDo(print());

		reset(customerService);
	}

	/**
	 * Verify a valid Customer Id to delete
	 *
	 * Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#deleteCustomer(java.lang.Long)}.
	 * 
	 * @throws Exception
	 */

	@Test
	public void whenDeleteValidCustomer_thenRemoveCustomer() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.get(customer.getId()))
				.willReturn(Optional.of(customer));

		// Verification
		mvc.perform(delete("/api/customers/" + customer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andDo(print());

		verify(customerService, VerificationModeFactory.times(1)).delete(customer.getId());
		reset(customerService);
	}

	/**
	 * Verify an invalid Customer Id to delete
	 * 
	 ** Test method for
	 * {@link com.sayedbaladoh.phonenumbers.controller.CustomerController#deleteCustomer(java.lang.Long)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void whenDeleteInvalidCustomerId_thenNotRemoveCustomer() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer.setId(1l);

		given(customerService.get(customer.getId()))
				.willReturn(Optional.of(customer));

		// Verification
		mvc.perform(delete("/api/customers/50")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404))
				.andDo(print());

		reset(customerService);
	}

	private Customer getCustomer(String name, String phoneNumber, String email, String gender, String countryCode) {

		Customer customer = new Customer();
		customer.setName(name);
		customer.setPhone(phoneNumber);
		customer.setEmail(email);
		customer.setGender(gender);
		customer.setCountryCode(countryCode);
		customer.setCreatedAt(Instant.now());
		customer.setUpdatedAt(Instant.now());
		return customer;
	}
}
