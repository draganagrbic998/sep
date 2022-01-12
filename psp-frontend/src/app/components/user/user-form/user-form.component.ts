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
export class UserFormComponent {

  constructor(
    public service: UserService,
    private roleService: RoleService,
    private paymentMethodService: PaymentMethodService,
    private webshopService: WebshopService,
    private route: ActivatedRoute
  ) { }

  get isEditing(){
    return !!+this.route.snapshot.params.id
  }

  entity = 'User'
  listRoute = Route.USERS

  formConfig: FormConfig = {
    email: {
      validation: 'required',
      disabled: this.isEditing
    },
    password: {
      type: 'password',
      validation: 'required',
      disabled: this.isEditing
    },
    role: {
      type: 'select',
      validation: 'required',
      options: this.roleService.read(),
      optionValue: 'name',
      optionKey: 'name',
      disabled: this.isEditing
    },
    methods: {
      type: 'multi-select',
      validation: 'required',
      options: this.paymentMethodService.read(),
      optionKey: 'name',
      hidding: {
        field: 'role',
        values: ['psp-admin', 'ws-admin', '']
      }
    },
    webshop: {
      type: 'select',
      validation: 'required',
      options: this.webshopService.read(),
      optionKey: 'name',
      optionValue: 'url',
      hidding: {
        field: 'role',
        values: ['psp-admin', '']
      },
      disabled: this.isEditing
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
