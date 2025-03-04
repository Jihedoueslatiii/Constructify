import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { Supplier } from '../../model/supplier.module';

@Component({
  selector: 'app-supplier-stats',
  templateUrl: './supplier-stats.component.html',
  styleUrls: ['./supplier-stats.component.css']
})
export class SupplierStatsComponent implements OnInit {
  supplierStats: any = {}; // Holds supplier financial data
  statusDistribution: { [key: string]: number } = {}; // Holds supplier status counts
  isLoading: boolean = true; // Loading state
  errorMessage: string = ''; // Error message
  top5Suppliers: Supplier[] = [];
  
  // Scatter Chart Configuration
  chartData: ChartConfiguration<'scatter'>['data'] = {
    datasets: []
  };
  chartOptions: ChartOptions<'scatter'> = {}; 

  // Pie Chart Configuration
  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: [],
    datasets: [
      {
        data: [],
        backgroundColor: ['#5b7cfa', '#3659db', '#35d8ac', '#4BC0C0', '#2A5D89', '#1E3A5F'], // Updated blue and green color palette
      },
    ],
  };
  pieChartOptions: ChartOptions<'pie'> = { responsive: true };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchSupplierStats();
    this.fetchSupplierStatusDistribution();
    this.fetchTop5Suppliers();
  }

  fetchSupplierStats() {
    this.http.get<any>('http://localhost:8089/SupplierContracts/api/suppliers/financial-health').subscribe(
      (data) => {
        this.supplierStats = data;
        this.isLoading = false;
        this.initializeChart(); // Initialize scatter chart
      },
      (error) => {
        this.errorMessage = 'Failed to fetch supplier stats. Please try again later.';
        this.isLoading = false;
        console.error('Error fetching supplier stats:', error);
      }
    );
  }

  fetchSupplierStatusDistribution() {
    this.http.get<{ [key: string]: number }>('http://localhost:8089/SupplierContracts/api/suppliers/status-distribution').subscribe(
      (data) => {
        this.statusDistribution = data;
        this.initializePieChart(); // Initialize pie chart after fetching data
      },
      (error) => {
        console.error('Error fetching supplier status distribution:', error);
      }
    );
  }

  initializeChart() {
    if (!this.supplierStats || !this.supplierStats.reliabilityVsContractValue) {
      console.error('No data available for scatter chart.');
      return;
    }

    this.chartData = {
      datasets: [
        {
          label: 'Reliability Score vs. Contract Value',
          data: this.supplierStats.reliabilityVsContractValue.map((item: any) => ({
            x: item.reliabilityScore,
            y: item.totalContractValue,
          })),
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgba(75, 192, 192, 1)',
          pointRadius: 5,
          pointHoverRadius: 7,
        },
      ],
    };

    this.chartOptions = {
      responsive: true,
      scales: {
        x: {
          type: 'linear',
          position: 'bottom',
          title: {
            display: true,
            text: 'Reliability Score',
          },
        },
        y: {
          title: {
            display: true,
            text: 'Total Contract Value',
          },
        },
      },
    };
  }

  initializePieChart() {
    if (!this.statusDistribution || Object.keys(this.statusDistribution).length === 0) {
      console.error('No data available for pie chart.');
      return;
    }

    this.pieChartData = {
      labels: Object.keys(this.statusDistribution), // Supplier status labels (e.g., "Active", "Inactive")
      datasets: [
        {
          data: Object.values(this.statusDistribution), // Counts of each status
          backgroundColor: ['#5b7cfa', '#3659db', '#35d8ac', '#4BC0C0', '#2A5D89', '#1E3A5F'], // Updated blue and green color palette
        },
      ],
    };
  }

  fetchTop5Suppliers() {
    this.http.get<Supplier[]>('http://localhost:8089/SupplierContracts/api/suppliers/top5-by-reliability').subscribe(
      (data) => {
        this.top5Suppliers = data;
      },
      (error) => {
        console.error('Error fetching top 5 suppliers:', error);
      }
    );
  }
}