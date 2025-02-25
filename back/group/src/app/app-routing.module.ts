import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewRessourceComponent } from './Views/Ressource/view-ressource/view-ressource.component';
import { AddRessourceComponent } from './Views/Ressource/add-ressource/add-ressource.component';
import { AddDeliverableComponent } from './Views/delivrable/add-deliverable/add-deliverable.component';
import { ViewDeliverableComponent } from './Views/delivrable/view-deliverable/view-deliverable.component';
import { UpdateDeliverableComponent } from './Views/delivrable/update-deliverable/update-deliverable.component';


const routes: Routes = [
  {path:'ViewRessource',component:ViewRessourceComponent},
  {path:'AddRessource',component:AddRessourceComponent},
  {path:'AddDeliverable',component:AddDeliverableComponent},
  {path:'ViewDeliverable',component:ViewDeliverableComponent},
  {path:'UpdateDeliverable/:id',component:UpdateDeliverableComponent},

  

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
