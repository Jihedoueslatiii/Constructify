import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { SupplierService } from '../../service/supplier.service';
import { Router } from '@angular/router';
import { Supplier } from '../../model/supplier.module';

@Component({
  selector: 'app-view-supplier',
  templateUrl: './view-supplier.component.html',
  styleUrls: ['./view-supplier.component.css']
})
export class ViewSupplierComponent implements OnInit {
  suppliers: Supplier[] = [];
  filteredSuppliers: Supplier[] = [];
  preferredSuppliers: Supplier[] = [];
  underReviewSuppliers: Supplier[] = [];
  blacklistedSuppliers: Supplier[] = [];
  successMessage: string = '';
  searchQuery: string = '';

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
        this.suppliers = data;
        this.filterSuppliers();
      },
      error: (error) => {
        console.error('Error fetching suppliers:', error);
      }
    });
  }

  filterSuppliers(): void {
    this.filteredSuppliers = this.suppliers.filter(s =>
      s.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      s.contactPerson.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      s.email.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      s.phoneNumber.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      s.address.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      s.status.toLowerCase().includes(this.searchQuery.toLowerCase())
    );

    this.preferredSuppliers = this.suppliers.filter(s => s.status === 'Preferred');
    this.underReviewSuppliers = this.suppliers.filter(s => s.status === 'Under Review');
    this.blacklistedSuppliers = this.suppliers.filter(s => s.status === 'Blacklisted');
  }

  drop(event: CdkDragDrop<Supplier[]>, newStatus: string): void {
    console.log("Previous container:", event.previousContainer);
    console.log("Current container:", event.container);

    if (!event.previousContainer.data || !event.container.data) {
      console.error("Drop event data is undefined", event);
      return;
    }

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );

      // Ensure the supplier exists in the new container
      if (!event.container.data[event.currentIndex]) {
        console.error("Supplier not found in the new container after drop.");
        return;
      }

      // Update supplier status
      const supplier = event.container.data[event.currentIndex];
      supplier.status = newStatus;

      // Update the supplier on the server
      this.supplierService.updateSupplier(supplier.idSupplier!, supplier).subscribe({
        next: () => {
          console.log('Supplier status updated');
          this.fetchSuppliers(); // Re-fetch to avoid inconsistencies
        },
        error: (error) => console.error('Error updating supplier:', error),
      });
    }
  }

  deleteSupplier(id: number): void {
    const confirmDelete = confirm('Are you sure you want to delete this supplier?');
    if (confirmDelete) {
      this.supplierService.deleteSupplier(id).subscribe({
        next: () => {
          this.successMessage = 'Supplier deleted successfully!';
          setTimeout(() => {
            this.clearSuccessMessage();
          }, 5000);
          this.fetchSuppliers();
        },
        error: (error) => {
          console.error('Error deleting supplier:', error);
        }
      });
    }
  }

  clearSuccessMessage(): void {
    this.successMessage = '';
  }

  openSupplierDetails(supplier: Supplier): void {
    this.router.navigate(['/supplier-details', supplier.idSupplier]);
  }
}