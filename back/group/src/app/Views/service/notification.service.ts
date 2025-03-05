import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<string[]>([]);
  notifications$ = this.notificationsSubject.asObservable();

  constructor() { }

  // Ajouter une notification
  addNotification(message: string): void {
    const notifications = this.notificationsSubject.getValue();
    notifications.push(message);
    this.notificationsSubject.next(notifications);
  }

  // Supprimer une notification individuelle
  removeNotification(notification: string): void {
    const notifications = this.notificationsSubject.getValue();
    const index = notifications.indexOf(notification);
    if (index > -1) {
      notifications.splice(index, 1);
      this.notificationsSubject.next(notifications);
    }
  }

  // Supprimer toutes les notifications
  clearNotifications(): void {
    this.notificationsSubject.next([]);
  }
}