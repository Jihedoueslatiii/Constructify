import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { NotificationService } from '../service/notification.service'; // Importez le service de notification

@Component({
  selector: 'app-bar',
  templateUrl: './bar.component.html',
  styleUrls: ['./bar.component.css']
})
export class BarComponent implements OnInit {
  @Output() searchEvent = new EventEmitter<string>();

  isSidebarClosed = false;
  activeMenu: string | null = null;
  showNotifications = false; // Contrôle l'affichage des notifications
  notificationCount = 0; // Compteur de notifications
  notifications: string[] = []; // Liste des notifications

  constructor(private notificationService: NotificationService) {} // Injectez le service de notification

  ngOnInit(): void {
    // Abonnez-vous aux notifications pour mettre à jour la liste et le compteur
    this.notificationService.notifications$.subscribe(notifications => {
      this.notifications = notifications;
      this.notificationCount = notifications.length;
    });
  }

  // Basculer l'affichage de la barre latérale
  toggleSidebar() {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  // Basculer l'affichage des sous-menus
  toggleSubMenu(menu: string) {
    this.activeMenu = this.activeMenu === menu ? null : menu;
  }

  // Gérer les changements dans la barre de recherche
  onSearchChange(event: any) {
    console.log("Recherche saisie :", event.target.value);
    this.searchEvent.emit(event.target.value);
  }

  // Basculer l'affichage des notifications
  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
  }

  // Supprimer une notification individuelle
  removeNotification(notification: string): void {
    this.notificationService.removeNotification(notification);
  }

  // Supprimer toutes les notifications
  clearNotifications(): void {
    this.notificationService.clearNotifications();
  }
}