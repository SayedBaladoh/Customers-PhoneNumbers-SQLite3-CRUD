package com.sayedbaladoh.phonenumbers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sayedbaladoh.phonenumbers.model.Country;

/**
 * Country Repository extends <code>JpaRepository</code> provides JPA related
 * methods for standard data access layer in a standard DAO.
 * 
 * @author Sayed Baladoh
 *
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	public Optional<Country> findByAlpha2Code(String alpha2Code);

	public Optional<Country> findByAlpha3Code(String alpha3Code);

	public Optional<Country> findByIsd(String isd);

	Boolean existsByAlpha2Code(String alpha2Code);

	Boolean existsByAlpha3Code(String alpha3Code);

	Boolean existsByIsd(String isd);

}
