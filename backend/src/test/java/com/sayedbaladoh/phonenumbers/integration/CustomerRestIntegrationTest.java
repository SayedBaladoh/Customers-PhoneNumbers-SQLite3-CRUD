package com.sayedbaladoh.phonenumbers.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.sayedbaladoh.phonenumbers.PhoneNumbersApplication;
import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.repository.CustomerRepository;
import com.sayedbaladoh.phonenumbers.util.JsonUtil;

/**
 * Customer Rest Integration tests
 * 
 * Test the Customer rest web services' integration tests
 * 
 * @author Sayed Baladoh
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		properties = "spring.profiles.active=test",
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = PhoneNumbersApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(
		replace = Replace.NONE)
public class CustomerRestIntegrationTest {

	private final String API_URL = "/api/customers";
	private final Long INVALID_ID = -1L;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CustomerRepository repository;

	@AfterEach
	public void cleanUp() {
		// resetDb
		repository.deleteAll();
	}

	/**
	 * Validate get all customers
	 * 
	 * @throws Exception
	 */
	@Test
	// @Disabled
	public void givenCustomers_whenGetCustomers_thenReturnCustomersWithStatus200()
			throws Exception {

		Customer customer1 = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		Customer customer2 = getCustomer("Ahmed Mahmoud", "(20) 1235478912", "test2@test.com", "male", "EG");
		Customer customer3 = getCustomer("Sara Ahmed", "(20) 1235478921", "test3@test.com", "female", "EG");

		saveTestCustomer(customer1);
		saveTestCustomer(customer2);
		saveTestCustomer(customer3);

		// Method call and Verification
		mvc.perform(get(API_URL)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(3))))
				.andExpect(jsonPath("$.content[0].name", is(customer1.getName())))
				.andExpect(jsonPath("$.content[1].name", is(customer2.getName())))
				.andExpect(jsonPath("$.content[2].name", is(customer3.getName())));
	}

	/**
	 * Validate findById with valid Id
	 */
	@Test
	public void givenCustomers_whenGetCustomerByID_thenReturnCustomerWithStatus200() throws Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer = saveTestCustomer(customer);

		// Method call and Verification
		mvc.perform(get(API_URL + "/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.id").value(customer.getId()))
				.andExpect(jsonPath("$.name").value(customer.getName()))
				.andExpect(jsonPath("$.phone").value(customer.getPhone()));
	}

	/**
	 * Validate findById with invalid Id
	 */
	@Test
	public void givenCustomer_whenGetInavlidCustomerId_thenReturnNotFound() throws Exception {

		// Method call and Verification
		mvc.perform(get(API_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404));
	}

	/**
	 * Verify post valid Customer
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	// @Disabled
	public void whenValidInput_thenAddAndReturnAddedCustomer() throws IOException, Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");

		// Method call and Verification
		mvc.perform(post(API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil
						.toJson(customer)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name", is(customer.getName())))
				.andExpect(jsonPath("$.phone", is(customer.getPhone())))
				.andExpect(jsonPath("$.email", is(customer.getEmail())));

		List<Customer> found = repository.findAll();
		assertThat(found)
				.extracting(Customer::getName)
				.contains(customer.getName());
	}

	/**
	 * Verify post invalid Customer
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	// @Disabled
	public void whenInvalidInput_thenNotAdded() throws IOException, Exception {
		// Data preparation
		Customer customer = getCustomer("Mohamed Ahmed", "1235478915", "test.com", "male", "EG");

		// Method call and Verification
		mvc.perform(post(API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil
						.toJson(customer)))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.id").doesNotExist());
	}

	/**
	 * Verify Put valid Customer
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Transactional
	@Test
	// @Disabled
	public void whenValidInput_thenEditAndReturnUpdatedCustomer() throws IOException, Exception {
		// Data preparation
		Customer customerTest = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		Customer customer = saveTestCustomer(customerTest);
		customer.setName("Mohamed_test_update");
		customer.setEmail("test_update@test.com");

		// Method call and Verification
		mvc.perform(put(API_URL + "/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil
						.toJson(customer)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.id").value(customer.getId()))
				.andExpect(jsonPath("$.name", is(customer.getName())))
				.andExpect(jsonPath("$.phone", is(customer.getPhone())))
				.andExpect(jsonPath("$.email", is(customer.getEmail())));
	}

	/**
	 * Verify Put inValid Customer ID
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	// @Disabled
	public void whenInValidInput_thenNotEdited() throws IOException, Exception {
		// Data preparation
		Customer customerTest = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");

		customerTest = saveTestCustomer(customerTest);

		Customer customer = new Customer();
		customer.setId(customerTest.getId());
		customer.setName("Mohamed_test_update");
		customer.setEmail("test_update@test.com");

		// Method call and Verification
		mvc.perform(put(API_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil
						.toJson(customer)))
				.andExpect(status().is(404));
	}

	/**
	 * Verify Delete inValid Customer ID
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	// @Disabled
	public void whenInValidCustomerId_thenCustomerNotDeleted() throws IOException, Exception {
		// Method call and Verification
		mvc.perform(put(API_URL + "/" + INVALID_ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400));
	}

	/**
	 * Save Test Customer
	 * 
	 * @param customer
	 * @return
	 */
	private Customer saveTestCustomer(Customer customer) {
		return repository.saveAndFlush(customer);
	}

	private Customer getCustomer(String name, String phoneNumber, String email, String gender, String countryCode) {

		Customer customer = new Customer();
		customer.setName(name);
		customer.setPhone(phoneNumber);
		customer.setEmail(email);
		customer.setGender(gender);
		customer.setCountryCode(countryCode);
		// customer.setCreatedAt(Instant.now());
		// customer.setUpdatedAt(Instant.now());
		return customer;
	}
}
