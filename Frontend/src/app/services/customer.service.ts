import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8181/api/customers';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) { }

  getAll(params): Observable<any> {
    return this.http.get(baseUrl, { params });
  }

  get(id): Observable<any> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  getCountByCountryCode(): Observable<any> {
    return this.http.get(`${baseUrl}/count/country_code`);
  }

  findByName(name, params): Observable<any> {
    return this.http.get(`${baseUrl}/name/${name}`, { params });
  }
  
  findByPhone(phone): Observable<any> {
    return this.http.get(`${baseUrl}/phone/${phone}`);
  }

  findByCountryCode(countryCode, params): Observable<any> {
    return this.http.get(`${baseUrl}/country_code/${countryCode}`, { params });
  }

  checkPhoneAvailable(phone): Observable<any> {
    return this.http.get(`${baseUrl}/available/phone/${phone}`);
  }

  checkEmailAvailable(email): Observable<any> {
    return this.http.get(`${baseUrl}/available/email/${email}`);
  }

  create(data): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(id, data): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  delete(id): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  // deleteAll(): Observable<any> {
  //   return this.http.delete(baseUrl);
  // }

}
