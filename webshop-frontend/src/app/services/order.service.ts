import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../models/order';
import { StandardRestService } from './standard-rest.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService extends StandardRestService<Order> {

  constructor(
    protected http: HttpClient
  ) {
    super(http, 'orders');
  }
}
