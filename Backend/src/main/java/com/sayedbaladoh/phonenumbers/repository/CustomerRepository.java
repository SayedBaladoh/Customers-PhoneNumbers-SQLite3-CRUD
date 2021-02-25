package com.sayedbaladoh.phonenumbers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sayedbaladoh.phonenumbers.model.Customer;
import com.sayedbaladoh.phonenumbers.model.CustomerCountryStatistics;

/**
 * Customer Repository extends <code>JpaRepository</code> provides JPA related
 * methods for standard data access layer in a standard DAO.
 * 
 * @author Sayed Baladoh
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public Optional<Customer> findByPhone(String phone);

	Boolean existsByPhone(String phone);

	Boolean existsByEmail(String email);

	Page<Customer> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Customer> findAllByCountryCodeIgnoreCase(String countryCode, Pageable pageable);

//	@Query("SELECT c.countryCode AS countryCode, COUNT(c.id) AS count "
//			+ "FROM Customer AS c GROUP BY c.countryCode ORDER BY COUNT(c.id) DESC")
	@Query(value = "SELECT c.country_code AS countryCode, COUNT(c.id) AS count "
			  + "FROM customers AS c GROUP BY c.country_code ORDER BY count DESC", nativeQuery = true)
	List<CustomerCountryStatistics> findCustomersCountByCountryCode();

}
