import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewSupplierComponent } from './Views/Supplier/view-supplier/view-supplier.component';
import { AddSupplierComponent } from './Views/Supplier/add-supplier/add-supplier.component';
import { UpdateSupplierComponent } from './Views/Supplier/update-supplier/update-supplier.component';

const routes: Routes = [
  { path: 'view-supplier', component: ViewSupplierComponent },
  { path: 'add-supplier', component: AddSupplierComponent },
  { path: 'update-supplier/:id', component: UpdateSupplierComponent }, 
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}