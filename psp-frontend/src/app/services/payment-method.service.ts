import { Injectable } from '@angular/core'
import { StandardRestService } from './standard-rest.service'
import { PaymentMethod } from '../models/payment-method'
import { HttpClient } from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class PaymentMethodService extends StandardRestService<PaymentMethod> {
  constructor (protected http: HttpClient) {
    super(http, 'payment-methods')
  }
}
