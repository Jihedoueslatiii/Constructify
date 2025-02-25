import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-bar',
  templateUrl: './bar.component.html',
  styleUrls: ['./bar.component.css']
})
export class BarComponent {
  @Output() searchEvent = new EventEmitter<string>();

  isSidebarClosed = false;
  activeMenu: string | null = null;

  toggleSidebar() {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  toggleSubMenu(menu: string) {
    this.activeMenu = this.activeMenu === menu ? null : menu;
  }

  onSearchChange(event: any) {
    console.log("Recherche saisie :", event.target.value);
    this.searchEvent.emit(event.target.value);
  }
  
}
