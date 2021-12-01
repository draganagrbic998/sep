import { Component, OnInit } from '@angular/core';
import { CartItem } from 'src/app/models/cart-item';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-cart-list',
  templateUrl: './cart-list.component.html',
  styleUrls: ['./cart-list.component.scss']
})
export class CartListComponent implements OnInit {

  constructor(
    private cartService: CartService
  ) { }

  cart: CartItem[];

  async readCart() {
    this.cart = await this.cartService.read().toPromise();
  }

  ngOnInit() {
    this.readCart()
  }

}
