import { Component, OnInit } from '@angular/core';
import { ProblemRisk, ProblemRiskService } from 'src/app/Views/service/problem-risk.service';

@Component({
  selector: 'app-problem-risk-list',
  templateUrl: './problem-risk-list.component.html',
  styleUrls: ['./problem-risk-list.component.css']
})
export class ProblemRiskListComponent implements OnInit {

  problemRisks: ProblemRisk[] = [];

  constructor(private problemRiskService: ProblemRiskService) { }

  ngOnInit(): void {
    this.loadProblemRisks();
  }

  loadProblemRisks(): void {
    this.problemRiskService.getAllProblemRisks().subscribe({
      next: (data) => {
        this.problemRisks = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des ProblemRisk :', err);
      }
    });
  }

  deleteProblemRisk(id?: number): void {
    if (!id) return;
    if (confirm('Voulez-vous supprimer ce ProblemRisk ?')) {
      this.problemRiskService.deleteProblemRisk(id).subscribe({
        next: () => {
          this.loadProblemRisks();
        },
        error: (err) => {
          console.error('Erreur lors de la suppression :', err);
        }
      });
    }
  }
}
