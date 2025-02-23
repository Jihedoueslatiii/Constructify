import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Report } from '../model/report.module';
@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  private apiUrl = 'http://localhost:8090/Reports/api/reports/all';

  constructor(private http: HttpClient) { }

  // Method to get the reports
  getReports(): Observable<Report[]> {  // Use the correct type Report[] instead of any[]
    return this.http.get<Report[]>(this.apiUrl);
  }

  // Method to delete a report by its ID
  deleteReport(id: number): Observable<void> {
    const deleteUrl = `${this.apiUrl}/${id}`;  // Assuming the API uses the ID for deletion
    return this.http.delete<void>(deleteUrl);  // Returns void after deletion
  }
}
