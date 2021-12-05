import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentMethodService } from 'src/app/services/payment-method.service';
import { RoleService } from 'src/app/services/role.service';
import { UserService } from 'src/app/services/user.service';
import { WebshopService } from 'src/app/services/webshop.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-user-form',
  template: `<app-form [config]="this"></app-form>`
})
export class UserFormComponent  {

  constructor(
    public service: UserService,
    private roleService: RoleService,
    private paymentMethodService: PaymentMethodService,
    private webshopService: WebshopService,
    private route: ActivatedRoute
  ) { }

  listRoute = Route.USERS
  entity = 'User'

  formConfig: FormConfig = {
    email: {
      validation: 'required'
    },
    password: {
      validation: 'required',
      type: 'password',
      hide: !!+this.route.snapshot.params.id
    },
    role: {
      validation: 'required',
      options: this.roleService.read(),
      type: 'select',
      optionValue: 'name',
      optionKey: 'name'
    },
    methods: {
      options: this.paymentMethodService.read(),
      type: 'multi-select',
      optionKey: 'name',
      hidding: {
        field: 'role',
        values: ['psp-admin', 'ws-admin', '']
      },
      validation: 'required'
    },
    webshop: {
      options: this.webshopService.read(),
      type: 'select',
      optionKey: 'name',
      optionValue: 'url',
      hidding: {
        field: 'role',
        values: ['psp-admin', '']
      },
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
