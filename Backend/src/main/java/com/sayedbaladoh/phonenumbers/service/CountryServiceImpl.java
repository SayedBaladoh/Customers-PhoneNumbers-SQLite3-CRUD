package com.sayedbaladoh.phonenumbers.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sayedbaladoh.phonenumbers.model.Country;
import com.sayedbaladoh.phonenumbers.repository.CountryRepository;

/**
 * Country Service implementation
 * 
 * @author Sayed Baladoh
 *
 */
@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Override
	public Country save(Country Country) {
		return countryRepository.save(Country);
	}

	@Override
	public Page<Country> getAll(Pageable pageable) {
		return countryRepository.findAll(pageable);
	}

	@Override
	public Optional<Country> get(Long id) {
		return countryRepository.findById(id);
	}

	@Override
	public Optional<Country> getByAlpha2Code(String alpha2Code) {
		return countryRepository.findByAlpha2Code(alpha2Code);
	}

	@Override
	public Optional<Country> getByAlpha3Code(String alpha3Code) {
		return countryRepository.findByAlpha3Code(alpha3Code);
	}

	@Override
	public Optional<Country> getByIsd(String isd) {
		return countryRepository.findByIsd(isd);
	}

	@Override
	public boolean existsByAlpha2Code(String alpha2Code) {
		return countryRepository.existsByAlpha2Code(alpha2Code);
	}

	@Override
	public boolean existsByAlpha3Code(String alpha3Code) {
		return countryRepository.existsByAlpha3Code(alpha3Code);
	}

	@Override
	public boolean existsByIsd(String isd) {
		return countryRepository.existsByIsd(isd);
	}

	@Override
	public void delete(Long id) {
		countryRepository.deleteById(id);
	}
}
