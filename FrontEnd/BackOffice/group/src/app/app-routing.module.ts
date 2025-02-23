import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewSupplierComponent } from './Views/Supplier/view-supplier/view-supplier.component';
import { AddSupplierComponent } from './Views/Supplier/add-supplier/add-supplier.component';
import { UpdateSupplierComponent } from './Views/Supplier/update-supplier/update-supplier.component';
import { ViewContractsComponent } from './Views/Supplier/view-contracts/view-contracts.component';
import { ViewReportsComponent } from './Reports/viewreports/viewreports.component';

const routes: Routes = [
  { path: 'view-supplier', component: ViewSupplierComponent },
  { path: 'add-supplier', component: AddSupplierComponent },
  { path: 'update-supplier/:id', component: UpdateSupplierComponent },
  { path: 'view-contracts', component: ViewContractsComponent },
  { path: 'contract-details/:id', component: ViewContractsComponent },
  { path: 'view-reports', component: ViewReportsComponent },
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}