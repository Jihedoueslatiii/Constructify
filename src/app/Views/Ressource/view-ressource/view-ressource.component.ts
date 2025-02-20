import { Component, OnInit } from '@angular/core';
import { RessourceService } from '../../service/ressource.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view-ressource',
  templateUrl: './view-ressource.component.html',
  styleUrls: ['./view-ressource.component.css']
})
export class ViewRessourceComponent implements OnInit {
  listRessource: { idRessource: number; nomRessource: string; nombreRessource: number; typesRessource: string }[] = [];

  constructor(
    private rs: RessourceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadRessources();
    
  }

  loadRessources(): void {
    this.rs.getRessource().subscribe({
      next: (res) => this.listRessource = res,
      error: (err) => console.error('Erreur de récupération des ressources:', err)
    });
  }

  deleteRessource(id: number): void {
    if (confirm("Voulez-vous vraiment supprimer cette ressource ?")) {
      this.rs.deleteRessource(id).subscribe({
        next: () => {
          this.listRessource = this.listRessource.filter(res => res.idRessource !== id);
        },
        error: (err) => console.error('Erreur lors de la suppression de la ressource:', err)
      });
    }
  }
  
 
}
