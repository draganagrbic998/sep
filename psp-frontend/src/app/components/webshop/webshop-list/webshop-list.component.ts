import { Component } from '@angular/core'
import { WebshopService } from 'src/app/services/webshop.service'

@Component({
  selector: 'app-webshop-list',
  template: `<app-list [config]="this"></app-list>`
})
export class WebshopListComponent {
  constructor(
    public service: WebshopService
  ) { }

  hideCreate = true;
  hideEdit = true;
  hideDelete = true;

  columnMappings: { [key: string]: string } = {
    name: 'Shop Name',
    url: 'Shop Site'
  }

}
