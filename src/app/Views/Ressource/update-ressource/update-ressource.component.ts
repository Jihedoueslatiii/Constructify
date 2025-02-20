import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RessourceService } from '../../service/ressource.service';
import { Ressource } from '../../model/ressource.module';

@Component({
  selector: 'app-update-ressource',
  templateUrl: './update-ressource.component.html',
  styleUrls: ['./update-ressource.component.css']
})
export class UpdateRessourceComponent implements OnInit {
  ressource: Ressource = new Ressource(0, '', 0, '');

  constructor(
    private route: ActivatedRoute,
    private ressourceService: RessourceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.ressource.idRessource = id; // Assignez simplement l'ID à l'objet ressource
  }

  updateRessource(): void {
    this.ressourceService.updateRessource(this.ressource.idRessource, this.ressource)
      .subscribe({
        next: (response) => {
          console.log('Ressource mise à jour avec succès', response);
          this.router.navigate(['/ViewRessource']); // Redirection après mise à jour
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour de la ressource:', err);
        }
      });
  }
}
