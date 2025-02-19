import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Urls } from 'src/app/url/url';
import { Supplier } from '../model/supplier.module';

@Injectable({
  providedIn: 'root',
})
export class SupplierService {
  private apiUrl: string = Urls.serverpath1;

  constructor(private http: HttpClient) {}

  // Fetch all suppliers
  getSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>("http://localhost:8089/SupplierContracts/api/suppliers/all", {
      responseType: 'json'  // Vérifie que l'on attend du JSON
    });
  }
  

  // Create a new supplier
  createSupplier(supplier: Supplier): Observable<Supplier> {
    return this.http.post<Supplier>(`${this.apiUrl}/add`, supplier);
  }
  
  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
  
  updateSupplier(id: number, supplier: any): Observable<any> {
    return this.http.put<Supplier>(`${this.apiUrl}/update/${id}`, supplier);
  }

  
  
}