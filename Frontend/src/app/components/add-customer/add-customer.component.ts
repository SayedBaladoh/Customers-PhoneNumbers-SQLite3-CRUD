import { Component, OnInit } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';
// import {FormsModule} from '@angular/forms';
import {
  FormsModule,
  FormGroup,
  FormControl
} from '@angular/forms';
import { ViewChild } from '@angular/core';

@Component({
  selector: 'app-add-customer',
  templateUrl: './add-customer.component.html',
  styleUrls: ['./add-customer.component.css']
})

export class AddCustomerComponent implements OnInit {
  customer = {
    id: 0,
    name: '',
    country_code: '',
    phone: '',
    email: '',
    gender: ''
  };
  @ViewChild('f') form: any;
  submitted = false;
  genders = ['male', 'female'];
  countryCodePattern = "^[A-Z]{2}$";
  phonePattern = "^(\\(\\d{1,6}\\))[- .]?(\\d{3}[- .]?){2}\\d{1,4}$";
  emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";
  backendError = {
    name: '',
    country_code: '',
    phone: '',
    email: '',
    gender: ''
  };


  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
  }

  saveCustomer(): void {
    if (this.form.valid) {
      const data = {
        name: this.customer.name,
        country_code: this.customer.country_code,
        phone: this.customer.phone,
        email: this.customer.email,
        gender: this.customer.gender
      };
      this.customerService.create(data)
        .subscribe(
          response => {
            // console.log("Form Submitted!");
            // console.log(response);
            this.submitted = true;
            // this.form.reset();
          },
          error => {
            // console.log(error);
            let errors = error.error.errors;
            this.backendError = {
              name: '',
              country_code: '',
              phone: '',
              email: '',
              gender: ''
            };
            for (let err of errors) {
              if (err.includes("name:")) {
                this.form.controls['name'].setErrors({ backend: true });
                this.backendError.name += err + '\n ';
              } else if (err.includes("countryCode:")) {
                this.form.controls['country_code'].setErrors({ backend: true });
                this.backendError.country_code += err + '\n ';
              } else if (err.includes("phone:")) {
                this.form.controls['phone'].setErrors({ backend: true });
                this.backendError.phone += err + '\n ';
              } else if (err.includes("email:")) {
                this.form.controls['email'].setErrors({ backend: true });
                this.backendError.email += err + '\n ';
              } else if (err.includes("gender:")) {
                this.form.controls['gender'].setErrors({ backend: true });
                this.backendError.gender += err + '\n ';
              }
            }
          });
    }
  }

  newCustomer(): void {
    this.submitted = false;
    this.customer = {
      id: 0,
      name: '',
      country_code: '',
      phone: '',
      email: '',
      gender: ''
    };
  }

}
