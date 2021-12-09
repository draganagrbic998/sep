import { Component } from '@angular/core'
import { PaymentMethodService } from 'src/app/services/payment-method.service'
import { Route } from 'src/app/utils/route'

@Component({
  selector: 'app-payment-method-list',
  template: `
    <app-list [config]="this"></app-list>
  `
})
export class PaymentMethodListComponent {
  constructor(public service: PaymentMethodService) { }

  editRoute = Route.PAYMENT_METHOD_FORM
  hideEdit = true

  columnMappings: { [key: string]: string } = {
    name: 'Method Name'
  }
}
