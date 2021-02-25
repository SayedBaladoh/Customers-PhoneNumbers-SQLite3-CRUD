/**
 * 
 */
package com.sayedbaladoh.phonenumbers.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.repository.CustomerRepository;

/**
 * Customer Service Implementation units' test
 * 
 * @author Sayed Baladoh
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CustomerServiceImpTest {

	@TestConfiguration
	static class CustomerServiceImpTestContextConfiguration {

		@Bean
		public CustomerService customerService() {
			return new CustomerServiceImpl();
		}
	}

	@Autowired
	private CustomerService customerService;

	@MockBean
	private CustomerRepository customerRepository;

	private final Long INVALID_ID = -1L;
	private final Long FIRST_CUSTOMER_ID = 1L;
	private Customer customer1;

	@BeforeEach
	void setUp() {
		// Data preparation
		customer1 = getCustomer("Mohamed Ahmed", "(20) 1235478915", "test1@test.com", "male", "EG");
		customer1.setId(FIRST_CUSTOMER_ID);
		Customer customer2 = getCustomer("Ahmed Mahmoud", "(20) 1235478912", "test2@test.com", "male", "EG");
		Customer customer3 = getCustomer("Sara Ahmed", "(20) 1235478921", "test3@test.com", "female", "EG");

		List<Customer> allCustomers = Arrays.asList(customer1, customer2, customer3);
		Page<Customer> customersPage = new PageImpl<Customer>(allCustomers);

		Mockito.when(customerRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
		Mockito.when(customerRepository.getOne(customer1.getId())).thenReturn(customer1);
		Mockito.when(customerRepository.findAll(any(Pageable.class))).thenReturn(customersPage);
		Mockito.when(customerRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

		Mockito.when(customerRepository.existsByEmail(customer1.getEmail())).thenReturn(true);
		Mockito.when(customerRepository.existsByEmail(customer2.getEmail())).thenReturn(true);
		Mockito.when(customerRepository.existsByEmail("wrong_email")).thenReturn(false);

		Mockito.when(customerRepository.existsByPhone(customer1.getPhone())).thenReturn(true);
		Mockito.when(customerRepository.existsByPhone(customer2.getPhone())).thenReturn(true);
		Mockito.when(customerRepository.existsByPhone("wrong_phone_number")).thenReturn(false);

		Mockito.when(customerRepository.save(any(Customer.class))).thenReturn(customer1);
	}

	/**
	 * Validate get all customers
	 */
	@Test
	public void given3Customers_whenGetAll_thenReturn3Records() {

		// Method call
		Page<Customer> allCustomers = customerService.getAll(PageRequest.of(0, 5));

		// Verification
		assertThat(allCustomers.getContent())
				.hasSize(3)
				.extracting(Customer::getName)
				.contains("Mohamed Ahmed", "Ahmed Mahmoud", "Sara Ahmed");

		Mockito.verify(customerRepository, Mockito.times(1)).findAll(PageRequest.of(0, 5));
		Mockito.verifyNoMoreInteractions(customerRepository);
		Mockito.reset(customerRepository);
	}

	/**
	 * Validate get customer by Id, findById and customer exists
	 */
	// @Disabled
	@Test
	public void whenValidId_thenCustomerShouldBeFound() {
		// Method call
		Optional<Customer> customer = customerService.get(FIRST_CUSTOMER_ID);

		// Verification
		verifyFindByIdIsCalledOnce();
		assertThat(customer)
				.isNotNull()
				.isNotEmpty();
		assertThat(customer.get().getId())
				.isEqualTo(FIRST_CUSTOMER_ID);
		assertThat(customer.get().getName())
				.isEqualTo(customer1.getName());
	}

	/**
	 * Validate get customer by Id using invalid Id, findById and customer IsNull
	 */
	// @Disabled
	@Test
	public void whenInValidId_thenCustomerShouldNotBeFound() {
		// Method call
		Optional<Customer> customer = customerService.get(INVALID_ID);

		// Verification
		verifyFindByIdIsCalledOnce();
		assertThat(customer).isEmpty();
		assertThat(customer.isPresent()).isEqualTo(false);
	}

	/**
	 * Validate customer exists by email, existsByEmail
	 */
	@Test
	public void whenValidEmail_thenCustomerShouldExist() {
		String mail = customer1.getEmail();
		boolean doesCustomerExist = customerService.existsByEmail(mail);

		assertThat(doesCustomerExist).isEqualTo(true);

		Mockito.verify(customerRepository, VerificationModeFactory.times(1)).existsByEmail(mail);
		Mockito.reset(customerRepository);
	}

	@Test
	public void whenNonExistingEmail_thenCustomerShouldNotExist() {
		String wrongMail = "test_wrong@test.com";
		boolean doesCustomerExist = customerService.existsByEmail(wrongMail);

		assertThat(doesCustomerExist).isEqualTo(false);

		Mockito.verify(customerRepository, VerificationModeFactory.times(1)).existsByEmail(wrongMail);
		Mockito.reset(customerRepository);
	}

	/**
	 * Validate save customer with valid customer
	 */
	@Test
	public void whenValidCustomer_thenCustomerShouldBeSavedAndReturned() {
		// Data preparation
		Customer customer = customer1;

		// Method call
		Customer savedCustomer = customerService.save(customer);

		// Verification
		assertThat(savedCustomer)
				.isNotNull()
				.extracting(Customer::getId).isNotNull();
		assertThat(savedCustomer.getPhone())
				.isEqualTo(customer.getPhone());
		assertThat(savedCustomer.getEmail())
				.isEqualTo(customer.getEmail());
		assertThat(savedCustomer.getName())
				.isEqualTo(customer.getName());

		Mockito.verify(customerRepository, Mockito.times(1)).save(any(Customer.class));
		Mockito.verifyNoMoreInteractions(customerRepository);
	}

	/**
	 * Validate edit customer with valid customer
	 */
	@Test
	public void whenValidCustomer_thenCustomerShouldBeUpdatedAndReturned() {
		// Data preparation
		Customer customer = customer1;
		final String NEW_NAME = "Mahmoud Ahmed";
		customer.setName(NEW_NAME);

		// Method call
		Customer savedCustomer = customerService.save(customer);

		// Verification
		assertThat(savedCustomer)
				.isNotNull()
				.extracting(Customer::getId).isNotNull();
		assertThat(savedCustomer.getPhone())
				.isEqualTo(customer.getPhone());
		assertThat(savedCustomer.getEmail())
				.isEqualTo(customer.getEmail());
		assertThat(savedCustomer.getName())
				.isEqualTo(NEW_NAME);

		Mockito.verify(customerRepository, Mockito.times(1)).save(any(Customer.class));
		Mockito.verifyNoMoreInteractions(customerRepository);
	}

	/**
	 * Validate delete customer with valid customer Id
	 */
	@Test
	public void whenValidCustomer_thenCustomerShouldBeRemoved() {
		// Data preparation
		Long customerId = customer1.getId();

		// Method call
		customerService.delete(customerId);

		// Verification
		Mockito.verify(customerRepository, Mockito.times(1)).deleteById(customerId);
		Mockito.verifyNoMoreInteractions(customerRepository);
	}

	/**
	 * Verify FindById is called once
	 */
	private void verifyFindByIdIsCalledOnce() {
		Mockito.verify(customerRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(customerRepository);
		Mockito.reset(customerRepository);
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
