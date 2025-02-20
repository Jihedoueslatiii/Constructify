import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewRessourceComponent } from './Views/Ressource/view-ressource/view-ressource.component';
import { AddRessourceComponent } from './Views/Ressource/add-ressource/add-ressource.component';
import { UpdateRessourceComponent } from './Views/Ressource/update-ressource/update-ressource.component';

const routes: Routes = [
  {path:'ViewRessource',component:ViewRessourceComponent},
  {path:'AddRessource',component:AddRessourceComponent},
  {path:'UpdateRessource/:id',component:UpdateRessourceComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
