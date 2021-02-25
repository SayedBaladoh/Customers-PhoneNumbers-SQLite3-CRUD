package com.sayedbaladoh.phonenumbers.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sayedbaladoh.phonenumbers.model.Country;

/**
 * Country Service
 * 
 * @author Sayed Baladoh
 *
 */
public interface CountryService {

	Country save(Country country);

	Page<Country> getAll(Pageable pageable);

	Optional<Country> get(Long id);

	Optional<Country> getByAlpha2Code(String alpha2Code);

	Optional<Country> getByAlpha3Code(String alpha3Code);

	Optional<Country> getByIsd(String isd);

	boolean existsByAlpha2Code(String alpha2Code);

	boolean existsByAlpha3Code(String alpha3Code);

	boolean existsByIsd(String isd);

	void delete(Long id);
}
