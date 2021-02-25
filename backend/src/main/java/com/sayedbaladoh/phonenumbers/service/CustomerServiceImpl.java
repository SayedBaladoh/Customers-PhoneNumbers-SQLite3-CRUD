package com.sayedbaladoh.phonenumbers.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.model.CustomerCountryStatistics;
import com.sayedbaladoh.phonenumbers.repository.CustomerRepository;

/**
 * Customer Service implementation
 * 
 * @author Sayed Baladoh
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer save(Customer Customer) {
		return customerRepository.save(Customer);
	}

	@Override
	public Page<Customer> getAll(Pageable pageable) {
		return customerRepository.findAll(pageable);
	}

	@Override
	public Page<Customer> getAllByNameContains(String name, Pageable pageable) {
		return customerRepository.findAllByNameContainingIgnoreCase(name, pageable);
	}

	@Override
	public Page<Customer> getAllByCountryCode(String countryCode, Pageable pageable) {
		return customerRepository.findAllByCountryCodeIgnoreCase(countryCode, pageable);
	}

	@Override
	public Optional<Customer> get(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public Optional<Customer> getByPhone(String phone) {
		return customerRepository.findByPhone(phone);
	}

	@Override
	public List<CustomerCountryStatistics> getCountByCountryCode() {
		return customerRepository.findCustomersCountByCountryCode();
	}

	@Override
	public boolean existsByEmail(String email) {
		return customerRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByPhone(String phone) {
		return customerRepository.existsByPhone(phone);
	}

	@Override
	public void delete(Long id) {
		customerRepository.deleteById(id);
	}
}
