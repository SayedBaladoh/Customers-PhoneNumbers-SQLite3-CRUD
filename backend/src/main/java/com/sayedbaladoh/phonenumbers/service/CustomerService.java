package com.sayedbaladoh.phonenumbers.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.model.CustomerCountryStatistics;

/**
 * Customer Service
 * 
 * @author Sayed Baladoh
 *
 */
public interface CustomerService {

	Customer save(Customer customer);

	Page<Customer> getAll(Pageable pageable);

	Page<Customer> getAllByNameContains(String name, Pageable pageable);

	Page<Customer> getAllByCountryCode(String code, Pageable pageable);

	Optional<Customer> get(Long id);

	Optional<Customer> getByPhone(String phone);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	void delete(Long id);

	List<CustomerCountryStatistics> getCountByCountryCode();
}
