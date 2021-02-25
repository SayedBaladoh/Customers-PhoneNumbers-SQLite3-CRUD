import { Component, OnInit } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.css']
})
export class CustomerDetailsComponent implements OnInit {

  currentCustomer = null;
  genders = ['male', 'female'];
  countryCodePattern = "^[A-Z]{2}$";
  phonePattern = "^(\\(\\d{1,6}\\))[- .]?(\\d{3}[- .]?){2}\\d{1,4}$";
  emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";
  message = '';

  constructor(
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.message = '';
    this.getCustomer(this.route.snapshot.paramMap.get('id'));
  }

  getCustomer(id): void {
    this.customerService.get(id)
      .subscribe(
        data => {
          this.currentCustomer = data;
          // console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  updateCustomer(): void {
    this.customerService.update(this.currentCustomer.id, this.currentCustomer)
      .subscribe(
        response => {
          // console.log(response);
          this.message = 'The customer was updated successfully!';
        },
        error => {
          console.log(error);
          this.message = 'Soryy for error! '+error.message+' , '+error.error.errors;
        });
  }

  deleteCustomer(): void {
    this.customerService.delete(this.currentCustomer.id)
      .subscribe(
        response => {
          // console.log(response);
          this.router.navigate(['/customers']);
        },
        error => {
          console.log(error);
        });
  }

}
