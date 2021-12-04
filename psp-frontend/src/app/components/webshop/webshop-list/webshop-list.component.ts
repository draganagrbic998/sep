import { Component } from '@angular/core'
import { WebshopService } from 'src/app/services/webshop.service'
import { Route } from 'src/app/utils/route'

@Component({
  selector: 'app-webshop-list',
  template: `<app-list [config]="this"></app-list>`
})
export class WebshopListComponent {
  constructor (
    private webshopService: WebshopService
  ) {}

  get service () {
    return this.webshopService
  }

  editRoute = Route.WEBSHOP_FORM

  columnMappings: { [key: string]: string } = {
    name: 'Shop Name',
    url: 'Shop Site'
  }

}
