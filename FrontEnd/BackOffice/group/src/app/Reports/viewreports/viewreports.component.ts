import { Component, OnInit } from '@angular/core';
import { Report, ReportStatus } from 'src/app/Views/model/report.module';
import { ReportsService } from 'src/app/Views/service/reports.service';

@Component({
  selector: 'app-view-reports',
  templateUrl: './viewreports.component.html',
  styleUrls: ['./viewreports.component.css']
})
export class ViewReportsComponent implements OnInit {
  reports: Report[] = [];  
  searchTerm: string = '';  // Search input
  selectedStatus: ReportStatus | '' = ''; // Dropdown filter

  constructor(private reportsService: ReportsService) { }

  ngOnInit(): void {
    this.fetchReports();
  }

  fetchReports(): void {
    this.reportsService.getReports().subscribe(
      (data) => { 
        console.log('Fetched Reports:', data); // Debugging
        this.reports = data; 
      },
      (error) => { 
        console.error('Error fetching reports:', error); 
      }
    );
  }
  

  // Filter reports based on search and status
  filteredReports(): Report[] {
    return this.reports.filter(report => 
      (this.searchTerm === '' || report.title.toLowerCase().includes(this.searchTerm.toLowerCase())) &&
      (this.selectedStatus === '' || report.status === this.selectedStatus)
    );
  }

  // Delete a report
  deleteReport(id: number): void {
    if (confirm('Are you sure you want to delete this report?')) {
      this.reportsService.deleteReport(id).subscribe(() => {
        this.reports = this.reports.filter(report => report.idReport !== id);
      });
    }
  }
}
