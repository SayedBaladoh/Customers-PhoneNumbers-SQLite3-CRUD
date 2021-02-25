package com.sayedbaladoh.phonenumbers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sayedbaladoh.phonenumbers.validator.CountryCode;
import com.sayedbaladoh.phonenumbers.validator.CountryDialCode;
import com.sayedbaladoh.phonenumbers.validator.EmailTaken;
import com.sayedbaladoh.phonenumbers.validator.Gender;
import com.sayedbaladoh.phonenumbers.validator.PhoneTaken;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Customer entity. All details about the Customer.
 * 
 * Extends <code>DateAudit</code> to automatically populate createdAt and
 * updatedAt values when we persist an <code>Customer</code> entity.
 * 
 * @author Sayed Baladoh
 *
 */
@ApiModel(description = "The customer entity contains all details about the customer.")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers", uniqueConstraints = { @UniqueConstraint(columnNames = { "phone" }),
		@UniqueConstraint(columnNames = { "email" }) })
@DynamicUpdate(true)
public class Customer extends DateAudit{

	/**
	 * The customer Id.
	 */
	@ApiModelProperty(notes = "The generated customer Id.", hidden = true, readOnly = true)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
	/**
	 * The customer name.
	 */
	@ApiModelProperty(notes = "The customer name.")
	@NotBlank(message = "{required}")
	@Size(min = 3, max = 50)
	@Column(length = 50, nullable = false)
    private String name;
	
	/**
	 * The customer country code.
	 */
	@ApiModelProperty(notes = "The customer country code.")
	@CountryCode
	@JsonProperty("country_code")
	@Column(name = "country_code", length = 5)
	private String countryCode;
	
    /**
	 * The customer phone number.
	 */
	@ApiModelProperty(notes = "The customer phone number.")
	@NotBlank(message = "{required}")
	@Pattern(
			regexp = "^((\\(\\d{1,6}\\))|\\d{1,6})[- .]?(\\d{3}[- .]?){2}\\d{1,4}$", message = "{phone.invalid}")
	@CountryDialCode
	@Size(min = 3, max = 50)
	@PhoneTaken
	@Column(length = 50, nullable = false)
	private String phone;

	/**
	 * The customer email.
	 */
	@ApiModelProperty(notes = "The customer email")
	@Email(message = "{email.invalid}")
	@EmailTaken
	@Size(max = 50)
	@Column(length = 50)
	private String email;
	
	/**
	 * The customer gender.
	 */
	@ApiModelProperty(notes = "The customer gender male or female.")
	@Gender
	@Column(length = 9)
	private String gender;

}
