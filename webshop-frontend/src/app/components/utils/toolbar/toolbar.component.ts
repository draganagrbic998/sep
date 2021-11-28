import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent {

  constructor(
    private storageService: StorageService,
    private router: Router
  ) { }

  get login() {
    return this.router.url.includes(Route.LOGIN);
  }

  logout() {
    this.storageService.removeAuth();
    this.router.navigate([Route.LOGIN]);
  }

}
