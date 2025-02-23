import { Component, OnInit } from '@angular/core';
import { SupplierService } from '../../service/supplier.service';
import { Router } from '@angular/router';
import { Supplier } from '../../model/supplier.module';

@Component({
  selector: 'app-view-supplier',
  templateUrl: './view-supplier.component.html',
  styleUrls: ['./view-supplier.component.css']
})
export class ViewSupplierComponent implements OnInit {
  suppliers: Supplier[] = []; // Original list of suppliers
  filteredSuppliers: Supplier[] = []; // Filtered list of suppliers
  successMessage: string = '';
  searchQuery: string = ''; // Search query

  constructor(
    private supplierService: SupplierService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchSuppliers();
  }

  fetchSuppliers(): void {
    this.supplierService.getSuppliers().subscribe({
      next: (data: Supplier[]) => {
        console.log('Données reçues:', data);
        this.suppliers = data;
        this.filteredSuppliers = data; // Initialize filteredSuppliers with all suppliers
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des fournisseurs:', error);
      }
    });
  }

  deleteSupplier(id: number): void {
    const confirmDelete = confirm('Êtes-vous sûr de vouloir supprimer ce fournisseur ?');
    if (confirmDelete) {
      this.supplierService.deleteSupplier(id).subscribe({
        next: () => {
          console.log('Fournisseur supprimé avec succès');
          this.successMessage = 'Fournisseur supprimé avec succès !'; // Set success message

          // Clear the success message after 5 seconds
          setTimeout(() => {
            this.clearSuccessMessage();
          }, 5000);

          this.fetchSuppliers(); // Refresh the supplier list after deletion
        },
        error: (error) => {
          console.error('Erreur lors de la suppression du fournisseur:', error);
        }
      });
    }
  }

  clearSuccessMessage(): void {
    this.successMessage = ''; // Clear the success message
  }

  // Filter suppliers based on search query
  filterSuppliers(): void {
    if (!this.searchQuery) {
      this.filteredSuppliers = this.suppliers; // If search query is empty, show all suppliers
    } else {
      const query = this.searchQuery.toLowerCase();
      this.filteredSuppliers = this.suppliers.filter(
        (supplier) =>
          supplier.name.toLowerCase().includes(query) ||
          supplier.contactPerson.toLowerCase().includes(query) ||
          supplier.email.toLowerCase().includes(query) ||
          supplier.phoneNumber.toLowerCase().includes(query) ||
          supplier.address.toLowerCase().includes(query) ||
          supplier.status.toLowerCase().includes(query)
      );
    }
  }
}