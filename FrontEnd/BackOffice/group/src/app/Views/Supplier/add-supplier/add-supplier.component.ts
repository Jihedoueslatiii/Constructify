import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SupplierService } from '../../service/supplier.service';
import { Supplier } from '../../model/supplier.module';

@Component({
  selector: 'app-add-supplier',
  templateUrl: './add-supplier.component.html',
  styleUrls: ['./add-supplier.component.css']
})
export class AddSupplierComponent {
  supplierForm: FormGroup;
  successMessage: string | null = null; // Flag for success message

  // Define the list of supplier statuses
  statusList: string[] = ['Active', 'Inactive', 'Pending', 'Suspended'];

  constructor(
    private supplierService: SupplierService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    // Initialize form with no validation
    this.supplierForm = this.formBuilder.group({
      name: [''],
      contactPerson: [''],
      email: [''],
      phoneNumber: [''],
      address: [''],
      status: [''],  // Ensure status is available in the form
      industry: [''],
      taxId: [''],
      isPreferredSupplier: [false],
      blacklisted: [false],
    });
  }

  // Submit form to add supplier
  onSubmit(): void {
    if (this.supplierForm.valid) {
      const supplier: Supplier = this.supplierForm.value;

      // Call the service to add the supplier
      this.supplierService.createSupplier(supplier).subscribe(
        (response) => {
          // Set success message
          this.successMessage = 'Supplier added successfully!';

          // Clear the success message after 5 seconds
          setTimeout(() => {
            this.clearSuccessMessage();
          }, 5000);

          // Navigate to a different page after successful addition
          this.router.navigate(['/view-supplier']);
        },
        (error) => {
          console.error('Error adding supplier', error);
        }
      );
    }
  }

  clearSuccessMessage(): void {
    this.successMessage = '';
  }
}

