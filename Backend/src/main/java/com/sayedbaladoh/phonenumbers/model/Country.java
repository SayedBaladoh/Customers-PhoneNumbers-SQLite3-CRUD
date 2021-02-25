package com.sayedbaladoh.phonenumbers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sayedbaladoh.phonenumbers.validator.CountryCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Country entity. All details about the country.
 * 
 * 
 * @author Sayed Baladoh
 *
 */
@ApiModel(description = "The country entity contains all details about the country.")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
@DynamicUpdate(true)
public class Country {
	
	/**
	 * The country Id.
	 */
	@ApiModelProperty(notes = "The generated country Id.", hidden = true, readOnly = true)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
	/**
	 * The country name.
	 */
	@ApiModelProperty(notes = "The country name.")
	@NotBlank(message = "{required}")
	@Size(min = 3, max = 50)
	@Column(length = 50, nullable = false)
    private String name;
	
	/**
	 * The alpha 2 country code.
	 */
	@ApiModelProperty(notes = "The alpha 2 country code.")
	@CountryCode
	@JsonProperty("alpha_2_code")
	@Column(name = "alpha_2_code", length = 2, nullable = false)
	private String alpha2Code;
	
	/**
	 * The alpha 3 country code.
	 */
	@ApiModelProperty(notes = "The alpha 3 country code.")
	@JsonProperty("alpha_3_code")
	@Column(name = "alpha_3_code", length = 3, nullable = false)
	private String alpha3Code;
	
	
	/**
	 * The international standard dialing code.
	 */
	@ApiModelProperty(notes = "The country dial code.")
	@JsonProperty("isd_code")
	@Column(name = "isd_code", length = 5, nullable = false)
	private String isd;
}
