import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Urls } from 'src/app/url/url';
import { Supplier } from '../model/supplier.module';

@Injectable({
  providedIn: 'root',
})
export class SupplierService {
  private apiUrl: string = Urls.serverpath1; // http://localhost:8089/SupplierContracts/api/suppliers

  constructor(private http: HttpClient) {}

  // Corrected methods
  getSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(`${this.apiUrl}/all`); // http://localhost:8089/SupplierContracts/api/suppliers/all
  }

  createSupplier(supplier: Supplier): Observable<Supplier> {
    return this.http.post<Supplier>(`${this.apiUrl}/add`, supplier); // http://localhost:8089/SupplierContracts/api/suppliers/add
  }

  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`); // http://localhost:8089/SupplierContracts/api/suppliers/delete/{id}
  }

  updateSupplier(idSupplier: number, supplier: Supplier): Observable<Supplier> {
    return this.http.put<Supplier>(`${this.apiUrl}/update/${idSupplier}`, supplier); // http://localhost:8089/SupplierContracts/api/suppliers/update/{idSupplier}
  }

  getFinancialHealthStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/financial-health`); // http://localhost:8089/SupplierContracts/api/suppliers/financial-health
  }

  // Fetch supplier status distribution (for Pie Chart)
  getSupplierStatusDistribution(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/status-distribution`); // http://localhost:8089/SupplierContracts/api/suppliers/status-distribution
  }

  getTop5SuppliersByReliabilityScore(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(`${this.apiUrl}/top5-by-reliability`);
  }
}