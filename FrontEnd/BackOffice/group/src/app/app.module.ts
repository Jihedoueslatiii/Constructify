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
import { ViewContractsComponent } from './Views/Supplier/view-contracts/view-contracts.component';
import { FilterStatusPipe } from './Views/model/filter-status.pipe';
import { ViewReportsComponent } from './Reports/viewreports/viewreports.component';
import { ReportsService } from './Views/service/reports.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { ReportPopupComponent } from './report-popup/report-popup.component';
import { UpdateReportComponent } from './Reports/updatereport/updatereport.component';



@NgModule({
  declarations: [
    AppComponent,
    FilterStatusPipe, 
    BarComponent,
    ViewSupplierComponent,
    AddSupplierComponent,
UpdateSupplierComponent,
DeleteSupplierComponent,
ViewContractsComponent,
ViewReportsComponent,
ReportPopupComponent,
UpdateReportComponent


],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    BrowserAnimationsModule,
    MatDialogModule
    
  ],
  providers: [
    ReportsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
