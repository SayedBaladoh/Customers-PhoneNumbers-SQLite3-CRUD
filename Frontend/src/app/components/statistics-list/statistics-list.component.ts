import { Component, OnInit } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-statistics-list',
  templateUrl: './statistics-list.component.html',
  styleUrls: ['./statistics-list.component.css']
})
export class StatisticsListComponent implements OnInit {

  statistics: any;

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.retrieveStatistics();
  }

  retrieveStatistics(): void {
    this.customerService.getCountByCountryCode()
      .subscribe(
        response => {
          this.statistics = response;
        },
        error => {
          console.log(error);
        });
  }

}
