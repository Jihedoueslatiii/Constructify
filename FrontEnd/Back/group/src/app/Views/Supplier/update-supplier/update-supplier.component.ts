import { Component, OnInit } from '@angular/core';
import { Supplier } from '../../model/supplier.module';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService } from '../../service/supplier.service';

@Component({
  selector: 'app-update-supplier',
  templateUrl: './update-supplier.component.html',
  styleUrls: ['./update-supplier.component.css']
})
export class UpdateSupplierComponent implements OnInit {
  supplier: Supplier = { idSupplier: 0, name: '', contactPerson: '', email: '', phoneNumber: '', address: '', status: '', industry: '', rating: 0, taxId: '', totalContractValue: 0, preferredSupplier: false, reliabilityScore: 0, blacklisted: false, tags: [], dateAdded: '', lastUpdated: '' };

  constructor(
    private route: ActivatedRoute,
    private supplierService: SupplierService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.supplier.idSupplier = id;
    }
  }

  updateSupplier(): void {
    if (this.supplier.idSupplier) {
      this.supplierService.updateSupplier(this.supplier.idSupplier, this.supplier).subscribe({
        next: () => {
          console.log('Fournisseur mis à jour avec succès');
          this.router.navigate(['/view-supplier']);
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour du fournisseur:', err);
        }
      });
    }
  }
}