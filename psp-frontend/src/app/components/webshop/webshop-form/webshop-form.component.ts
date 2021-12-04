import { Component } from '@angular/core';
import { WebshopService } from 'src/app/services/webshop.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-webshop-form',
  template: `<app-form [config]="this"></app-form>`
})
export class WebshopFormComponent  {

  constructor(
    private webshopService: WebshopService,
  ) { }

  service = this.webshopService
  listRoute = Route.WEBSHOPS
  entity = 'WebShop'

  pending = false;
  formConfig: FormConfig = {
    name: {
      validation: 'required'
    },
    url: {
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
