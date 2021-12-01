import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/models/order';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {

  constructor(
    private cartService: CartService
  ) { }

  orders: Order[];

  async readOrders() {
    this.orders = await this.cartService.orders().toPromise();
  }

  productsSummary(order: Order) {
    let summary = '';
    const products = {};
    for (const item of order.items) {
      const name = item.product.name[0].toUpperCase() + item.product.name.substr(1);
      if (!(item.product.id in products)) {
        products[name] = 0;
      }
      products[name] += item.quantity;
    }
    for (const product in products) {
      summary += `, ${product} (${products[product]})`
    }
    return summary.substr(2);
  }

  totalProductsCount(order: Order) {
    return order.items.reduce((a, b) => a + b.quantity, 0)
  }

  totalPrice(order: Order) {
    let summary = '';
    for (const item of order.items) {
      summary += `, ${item.quantity * item.product.price}${item.product.currency} (${item.product.name})`
    }
    return summary.substr(2);
  }

  ngOnInit() {
    this.readOrders();
  }

}
