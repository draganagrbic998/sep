import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CartItem } from '../models/cart-item';
import { StandardRestService } from './standard-rest.service';

@Injectable({
  providedIn: 'root'
})
export class CartService extends StandardRestService<CartItem> {

  constructor(
    protected http: HttpClient
  ) {
    super(http, 'cart');
  }

  addToCart(productId: number) {
    return this.http.put<void>(`${this.API_URL}/${productId}/add`, null);
  }

  removeFromCart(productId: number) {
    return this.http.put<void>(`${this.API_URL}/${productId}/remove`, null);
  }

}
