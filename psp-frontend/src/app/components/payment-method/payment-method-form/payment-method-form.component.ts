import { Component } from '@angular/core';
import { PaymentMethodService } from 'src/app/services/payment-method.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-payment-method-form',
  template: `<app-form [config]="this"></app-form>`
})
export class PaymentMethodFormComponent  {

  constructor(
    private paymentMethodService: PaymentMethodService
  ) { }

  service = this.paymentMethodService
  listRoute = Route.PAYMENT_METHODS
  entity = 'Payment Method'

  pending = false;
  formConfig: FormConfig = {
    name: {
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
