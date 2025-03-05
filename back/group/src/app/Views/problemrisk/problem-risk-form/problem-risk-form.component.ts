import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProblemRisk, ProblemRiskService } from 'src/app/Views/service/problem-risk.service';

@Component({
  selector: 'app-problem-risk-form',
  templateUrl: './problem-risk-form.component.html',
  styleUrls: ['./problem-risk-form.component.css']
})
export class ProblemRiskFormComponent implements OnInit {

  // On initialise detectionDate à new Date() (type Date, pas string)
  problemRisk: ProblemRisk = {
    title: '',
    description: '',
    type: 'TECHNICAL',
    probability: 'LOW',
    problemStatus: 'OPEN',
    detectionDate: new Date() // Par défaut, "maintenant"
    
  };

  isEditMode = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private problemRiskService: ProblemRiskService
  ) {}

  ngOnInit(): void {
    // Vérifier si on est en mode édition (ex: /edit-problem-risk/1)
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      const numericId = +id;
      this.problemRiskService.getProblemRiskById(numericId).subscribe({
        next: (data) => {
          // Les dates reçues sont souvent des chaînes JSON, ex: "2025-02-27T10:00:00"
          // Tu peux convertir manuellement si besoin :
          // data.detectionDate = new Date(data.detectionDate)
          this.problemRisk = data;
        },
        error: (err) => {
          console.error('Erreur lors de la récupération du ProblemRisk :', err);
        }
      });
    }
  }

  generateData(): void {
    const random = Math.floor(Math.random() * 1000);
    this.problemRisk.title = `Problème aléatoire #${random}`;
    this.problemRisk.description = `Description pour le problème #${random}`;
    this.problemRisk.type = this.getRandomType();
    this.problemRisk.probability = this.getRandomProbability();
    this.problemRisk.problemStatus = 'OPEN';
    this.problemRisk.detectionDate = new Date();
    this.problemRisk.appliedSolutions = '';
    this.problemRisk.resolutionDate = undefined;
  }

  getRandomType(): string {
    const types = ['TECHNICAL', 'MANAGEMENT', 'OTHER'];
    return types[Math.floor(Math.random() * types.length)];
  }

  getRandomProbability(): string {
    const probs = ['LOW', 'MEDIUM', 'HIGH'];
    return probs[Math.floor(Math.random() * probs.length)];
  }

  save(): void {
    if (this.isEditMode && this.problemRisk.idProblemRisk) {
      // Mise à jour
      this.problemRiskService.updateProblemRisk(this.problemRisk.idProblemRisk, this.problemRisk)
        .subscribe({
          next: () => {
            this.router.navigate(['/problem-risks']);
          },
          error: (err) => {
            console.error('Erreur lors de la mise à jour :', err);
          }
        });
    } else {
      // Ajout
      this.problemRiskService.addProblemRisk(this.problemRisk).subscribe({
        next: () => {
          this.router.navigate(['/problem-risks']);
        },
        error: (err) => {
          console.error('Erreur lors de l\'ajout :', err);
        }
      });
    }
  }
}
