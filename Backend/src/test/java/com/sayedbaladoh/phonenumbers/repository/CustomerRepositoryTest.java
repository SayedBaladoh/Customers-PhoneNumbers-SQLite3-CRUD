package com.sayedbaladoh.phonenumbers.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sayedbaladoh.phonenumbers.model.Customer;

/**
 * Customer Repository units' test
 * 
 * @author Sayed Baladoh
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = "spring.profiles.active=test")
@AutoConfigureTestDatabase(
		replace = Replace.NONE)
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Validate findAll
	 */
	// @Disabled
	@Test
	public void givenSetOfCustomers_whenFindAll_thenReturnPageofAllCustomers() {
		// Data preparation
		Customer customer1 = getCustomer();
		Customer customer2 = getCustomer("Ahmed Mahmoud", "(20) 1235478912", "test2@test.com", "male", "EG");
		Customer customer3 = getCustomer("Sara Ahmed", "(20) 1235478921", "test3@test.com", "female", "EG");

		customerRepository.save(customer1);
		customerRepository.save(customer2);
		customerRepository.save(customer3);

		// Method call
		Page<Customer> allCustomers = customerRepository.findAll(PageRequest.of(0, 3));

		// Verification
		assertThat(allCustomers.getContent())
				.hasSize(3)
				.extracting(Customer::getName)
				.containsOnly(customer1.getName(), customer2.getName(), customer3.getName());
	}

	/**
	 * Validate findById with valid Id
	 */
	// @Disabled
	@Test
	public void givenCustomerInDB_WhenFindById_ThenReturnOptionalWithCustomer() {

		// Data preparation
		Customer customer = getCustomer();
		customerRepository.save(customer);

		// Method call
		Optional<Customer> found = customerRepository.findById(customer.getId());

		// Verification
		assertThat(found.isPresent())
				.isEqualTo(true);
		assertThat(found.get().getPhone())
				.isEqualTo(customer.getPhone());
		assertThat(found.get().getEmail())
				.isEqualTo(customer.getEmail());
		assertThat(found.get().getName())
				.isEqualTo(customer.getName());
	}

	/**
	 * Validate findById with invalid Id
	 */
	// @Disabled
	@Test
	public void givenEmptyDB_WhenFindById_ThenReturnEmptyOptional() {
		// Method call
		Optional<Customer> foundCustomer = customerRepository.findById(-1L);

		// Verification
		assertThat(foundCustomer.isPresent()).isEqualTo(false);
	}

	/**
	 * Validate findByPhone with valid phone number
	 */
	@Test
	public void givenCustomerInDB_WhenFindByPhone_ThenReturnOptionalWithCustomer() {
		// Data preparation
		Customer customer = getCustomer();
		customerRepository.save(customer);

		// Method call
		Optional<Customer> found = customerRepository.findByPhone(customer.getPhone());

		// Verification
		assertThat(found.isPresent())
				.isEqualTo(true);
		assertThat(found.get().getPhone())
				.isEqualTo(customer.getPhone());
		assertThat(found.get().getName())
				.isEqualTo(customer.getName());
	}

	/**
	 * Validate findByPhone with invalid phone number
	 */
	@Test
	public void givenEmptyDB_WhenFindByPhone_ThenReturnEmptyOptional() {
		// Method call
		Optional<Customer> found = customerRepository.findByPhone("doesNotExist");

		// Verification
		assertThat(found.isPresent())
				.isEqualTo(false);
		assertThat(found.isEmpty());
	}

	/**
	 * Validate save customer with valid customer
	 */
	// @Disabled
	@Test
	public void whenValidCustomer_thenCustomerShouldBeSavedAndReturned() {
		// Data preparation
		Customer customer = getCustomer();

		// Method call
		Customer savedCustomer = customerRepository.save(customer);

		// Verification
		assertThat(savedCustomer)
				.isNotNull()
				.extracting(Customer::getId)
				.isNotNull()
				.isNotEqualTo("");
		assertThat(savedCustomer.getPhone())
				.isEqualTo(customer.getPhone());
		assertThat(savedCustomer.getEmail())
				.isEqualTo(customer.getEmail());
		assertThat(savedCustomer.getName())
				.isEqualTo(customer.getName());
	}

	/**
	 * Validate save customer with invalid customer
	 */
	// @Disabled
	@Test
	public void whenInvalidCustomer_thenCustomerShouldNotBeSaved() {

		Exception exception = Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			Customer savedCustomer = customerRepository.save(null);

			assertThat(savedCustomer)
					.isNull();
		});

		String expectedMessage = "Entity must not be null.";
		String actualMessage = exception.getMessage();

		assertThat(actualMessage.contains(expectedMessage));
	}

	/**
	 * Validate edit customer with valid customer
	 */
	// @Disabled
	@Test
	public void whenValidCustomer_thenCustomerShouldBeUpdatedAndReturned() {
		// Data preparation
		Customer customer = getCustomer();
		customerRepository.save(customer);

		// Method call
		Customer savedCustomer = customerRepository.save(customer);

		// Update Object
		String newName = "AHMED Muhammad";
		savedCustomer.setName(newName);

		// Method call
		Customer updatedCustomer = customerRepository.save(savedCustomer);

		// Verification
		assertThat(updatedCustomer)
				.isNotNull()
				.extracting(Customer::getId).isNotNull();
		assertThat(updatedCustomer.getPhone())
				.isEqualTo(savedCustomer.getPhone());
		assertThat(updatedCustomer.getEmail())
				.isEqualTo(savedCustomer.getEmail());
		assertThat(updatedCustomer.getEmail())
				.isEqualTo(savedCustomer.getEmail());
		assertThat(updatedCustomer.getName())
				.isEqualTo(newName);

	}

	/**
	 * Validate delete customer with valid customer Id
	 */
	@Test
	public void whenValidCustomerId_thenCustomerShouldBeRemoved() {
		// Data preparation
		Customer customer = getCustomer();
		customerRepository.save(customer);

		// Method call
		customerRepository.deleteById(customer.getId());
	}

	/**
	 * Validate delete customer with invalid customer Id
	 */
	@Test
	public void whenInvalidCustomerId_thenCustomerShouldNotBeRemoved() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			customerRepository.deleteById(-1l);
		});
	}

	@AfterEach
	public void cleanUp() {
		customerRepository.deleteAll();
	}

	private Customer getCustomer() {

		Customer customer = new Customer();
		customer.setName("Ahmed Muhammad");
		customer.setPhone("(20) 102345679");
		customer.setEmail("ahmed@test.com");
		customer.setGender("male");
		customer.setCountryCode("EG");
		customer.setCreatedAt(Instant.now());
		customer.setUpdatedAt(Instant.now());
		return customer;
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
