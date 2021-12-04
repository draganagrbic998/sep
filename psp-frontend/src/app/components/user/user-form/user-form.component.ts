import { Component } from '@angular/core';
import { map } from 'rxjs';
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
    private userService: UserService,
    private roleService: RoleService,
    private paymentMethodService: PaymentMethodService
  ) { }

  service = this.userService
  listRoute = Route.USERS
  entity = 'User'

  pending = false;
  formConfig: FormConfig = {
    email: {
      validation: 'required'
    },
    password: {
      validation: 'required',
      type: 'password'
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
        value: 'merchant'
      }
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
