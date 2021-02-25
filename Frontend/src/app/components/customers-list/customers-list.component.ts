import { Component, OnInit } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.css']
})
export class CustomersListComponent implements OnInit {

  customers: any;
  currentCustomer = null;
  currentIndex = -1;
  name = '';
  country_code = '';

  page = 1;
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.retrieveCustomers();
  }

  getRequestParams(page, pageSize): any {

    let params = {};

    if (page) {
      params[`page`] = page - 1;
    }

    if (pageSize) {
      params[`size`] = pageSize;
    }

    return params;
  }

  retrieveCustomers(): void {
    const params = this.getRequestParams(this.page, this.pageSize);

    this.customerService.getAll(params)
      .subscribe(
        response => {
          // const { customers, totalItems } = response;
          this.customers = response.content;
          this.count = response.totalElements;
          // console.log(response);
        },
        error => {
          console.log(error);
        });
  }

  retrieveCustomersByName(): void {
    this.country_code = '';
    const params = this.getRequestParams(this.page, this.pageSize);

    this.customerService.findByName(this.name, params)
      .subscribe(
        response => {
          this.customers = response.content;
          this.count = response.totalElements;
          // console.log(response);
        },
        error => {
          console.log(error);
        });
  }

  retrieveCustomersByCountryCode(): void {
    this.name = '';
    const params = this.getRequestParams(this.page, this.pageSize);

    this.customerService.findByCountryCode(this.country_code, params)
      .subscribe(
        response => {
          this.customers = response.content;
          this.count = response.totalElements;
          // console.log(response);
        },
        error => {
          console.log(error);
        });
  }

  handlePageChange(event): void {
    this.page = event;
    if (this.name) {
      this.retrieveCustomersByName();
    } else if (this.country_code) {
      this.retrieveCustomersByCountryCode();
    } else {
      this.retrieveCustomers();
    }
  }

  handlePageSizeChange(event): void {
    this.pageSize = event.target.value;
    this.page = 1;
    if (this.name) {
      this.retrieveCustomersByName();
    } else if (this.country_code) {
      this.retrieveCustomersByCountryCode();
    } else {
      this.retrieveCustomers();
    }
  }

  setActiveCustomer(customer, index): void {
    this.currentCustomer = customer;
    this.currentIndex = index;
  }

}
