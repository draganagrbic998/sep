import { Component } from '@angular/core';
import { PaymentMethodService } from 'src/app/services/payment-method.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-payment-method-form',
  template: `<app-form [config]="this"></app-form>`
})
export class PaymentMethodFormComponent {

  constructor(public service: PaymentMethodService) { }

  entity = 'Payment Method'
  listRoute = Route.PAYMENT_METHODS

  formConfig: FormConfig = {
    name: {
      type: 'select',
      validation: 'required',
      options: this.service.toAdd(),
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
