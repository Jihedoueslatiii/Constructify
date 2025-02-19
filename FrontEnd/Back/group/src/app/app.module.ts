import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BarComponent } from './Views/bar/bar.component';
import { ViewSupplierComponent } from './Views/Supplier/view-supplier/view-supplier.component';
import { AddSupplierComponent } from './Views/Supplier/add-supplier/add-supplier.component';
import { UpdateSupplierComponent } from './Views/Supplier/update-supplier/update-supplier.component';
import { DeleteSupplierComponent } from './Views/Supplier/delete-supplier/delete-supplier.component';


@NgModule({
  declarations: [
    AppComponent,
    BarComponent,
    ViewSupplierComponent,
    AddSupplierComponent,
UpdateSupplierComponent,
DeleteSupplierComponent

],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
