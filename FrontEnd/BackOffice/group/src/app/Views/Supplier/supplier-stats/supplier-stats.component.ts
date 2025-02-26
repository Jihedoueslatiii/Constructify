import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartDataset, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-supplier-stats',
  templateUrl: './supplier-stats.component.html',
  styleUrls: ['./supplier-stats.component.css']
})
export class SupplierStatsComponent implements OnInit {
  supplierStats: any; // Object to hold supplier stats
  isLoading: boolean = true; // Loading state
  errorMessage: string = ''; // Error message
  Object = Object;

  // Chart.js variables
  chartData: ChartConfiguration<'scatter'>['data'] = {
    datasets: [] // Initialize with an empty array
  };
  chartOptions: ChartOptions<'scatter'> = {}; // Initialize with an empty object

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchSupplierStats();
  }

  fetchSupplierStats() {
    this.http.get('http://localhost:8089/SupplierContracts/api/suppliers/financial-health').subscribe(
      (data) => {
        this.supplierStats = data;
        this.isLoading = false;
        this.initializeChart(); // Initialize the chart after data is fetched
      },
      (error) => {
        this.errorMessage = 'Failed to fetch supplier stats. Please try again later.';
        this.isLoading = false;
        console.error('Error fetching supplier stats:', error);
      }
    );
  }

  initializeChart() {
    // Prepare chart data
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

    // Configure chart options
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
}