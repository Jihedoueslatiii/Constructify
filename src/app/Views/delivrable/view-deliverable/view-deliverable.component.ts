import { Component, OnInit } from '@angular/core';
import { DeliverableService } from '../../service/deliverable.service';
import { Router } from '@angular/router';
import { DeliverableStatus } from '../../model/deliverable.module';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-view-deliverable',
  templateUrl: './view-deliverable.component.html',
  styleUrls: ['./view-deliverable.component.css']
})
export class ViewDeliverableComponent implements OnInit {
  DeliverableStatus = DeliverableStatus;
  listDeliverable: any[] = [];
  filteredDeliverables: any[] = [];
  searchTerm: string = '';
  dashboardStats: any = {};
  showDashboard = false;
  isKanbanView = false;

  // Colonnes Kanban adaptées
  todoDeliverables: any[] = []; // À faire (sans statut défini)
  inProgressDeliverables: any[] = [];
  completedDeliverables: any[] = [];
  validatedDeliverables: any[] = [];

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 6;
  totalItems: number = 0;
  pageSizeOptions: number[] = [3, 6, 9, 12];

  completionStatusThresholds = [
    { threshold: 90, label: 'Excellent', class: 'excellent' },
    { threshold: 70, label: 'Très bien', class: 'very-good' },
    { threshold: 50, label: 'En bonne voie', class: 'on-track' },
    { threshold: 30, label: 'Attention nécessaire', class: 'needs-attention' },
    { threshold: 0, label: 'Critique', class: 'critical' }
  ];

  constructor(
    private rs: DeliverableService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDeliverables();
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  loadDeliverables(): void {
    this.rs.getDeliverables().subscribe(
      res => {
        this.listDeliverable = res;
        this.filteredDeliverables = res;
        this.totalItems = this.filteredDeliverables.length;
        this.calculateDashboardStats();
        this.organizeKanbanColumns();
      },
      error => {
        console.error('Erreur de récupération des deliverables:', error);
      }
    );
  }

  organizeKanbanColumns(): void {
    this.todoDeliverables = this.listDeliverable.filter(d => 
      !d.status || d.status === ''
    );
    this.inProgressDeliverables = this.listDeliverable.filter(d => 
      d.status === DeliverableStatus.IN_PROGRESS
    );
    this.completedDeliverables = this.listDeliverable.filter(d => 
      d.status === DeliverableStatus.COMPLETED
    );
    this.validatedDeliverables = this.listDeliverable.filter(d => 
      d.status === DeliverableStatus.VALIDATED
    );
  }

  drop(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      
      const movedItem = event.container.data[event.currentIndex];
      let newStatus: DeliverableStatus | string;
      
      switch (event.container.id) {
        case 'todoList':
          newStatus = ''; // Pas de statut
          break;
        case 'inProgressList':
          newStatus = DeliverableStatus.IN_PROGRESS;
          break;
        case 'completedList':
          newStatus = DeliverableStatus.COMPLETED;
          break;
        case 'validatedList':
          newStatus = DeliverableStatus.VALIDATED;
          break;
        default:
          newStatus = movedItem.status;
      }
      
      this.updateDeliverable(movedItem.idDeliverable, { ...movedItem, status: newStatus });
    }
  }

  updateDeliverable(id: number, deliverable: any): void {
    this.rs.updateDeliverable(id, deliverable).subscribe({
      next: () => {
        console.log('Statut mis à jour avec succès');
      },
      error: (err: any) => {
        console.error('Erreur lors de la mise à jour du statut:', err);
        this.loadDeliverables();
      }
    });
  }

  toggleView(): void {
    this.isKanbanView = !this.isKanbanView;
  }

  toggleDashboard(): void {
    this.showDashboard = !this.showDashboard;
    if (this.showDashboard) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  onItemsPerPageChange(itemsPerPage: number): void {
    this.itemsPerPage = itemsPerPage;
    this.currentPage = 1;
  }

  get paginatedDeliverables(): any[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredDeliverables.slice(startIndex, startIndex + this.itemsPerPage);
  }

  calculateDashboardStats(): void {
    const now = new Date();
    const completed = this.listDeliverable.filter(d => 
      d.status === DeliverableStatus.COMPLETED || 
      d.status === DeliverableStatus.VALIDATED
    ).length;

    const delayed = this.listDeliverable.filter(d => 
      d.status === DeliverableStatus.IN_PROGRESS &&
      new Date(d.expected_date) < now
    ).length;

    this.dashboardStats = {
      total: this.listDeliverable.length,
      completed,
      inProgress: this.listDeliverable.filter(d => d.status === DeliverableStatus.IN_PROGRESS).length,
      delayed,
      completionRate: this.listDeliverable.length > 0 ? Math.round((completed / this.listDeliverable.length) * 100) : 0
    };
  }

  getCompletionStatus() {
    if (!this.dashboardStats.completionRate) {
      return { label: 'Non disponible', class: 'na' };
    }

    const rate = this.dashboardStats.completionRate;
    return this.completionStatusThresholds.find(t => rate >= t.threshold) || 
           { label: 'Inconnu', class: 'unknown' };
  }

  deleteDeliverable(id: number): void {
    if (confirm("Voulez-vous vraiment supprimer ce deliverable ?")) {
      this.rs.deleteDeliverable(id).subscribe({
        next: () => {
          this.listDeliverable = this.listDeliverable.filter(res => res.idDeliverable !== id);
          this.filterDeliverables();
          this.calculateDashboardStats();
          this.organizeKanbanColumns();
        },
        error: (err: any) => console.error('Erreur lors de la suppression du deliverable:', err)
      });
    }
  }

  filterDeliverables(): void {
    this.filteredDeliverables = this.listDeliverable.filter(deliverable =>
      deliverable.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      (deliverable.delivery_date && deliverable.delivery_date.toString().includes(this.searchTerm)) ||
      (deliverable.expected_date && deliverable.expected_date.toString().includes(this.searchTerm))
    );
    this.totalItems = this.filteredDeliverables.length;
    this.currentPage = 1;
  }
  
  onSearchChange(term: string): void {
    this.searchTerm = term;
    this.filterDeliverables();
  }
}