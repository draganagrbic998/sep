import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/models/order';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {

  constructor(
    private orderService: OrderService
  ) { }

  orders: Order[];

  async readOrders() {
    this.orders = await this.orderService.read().toPromise();
  }

  ngOnInit() {
    this.readOrders();
  }

}
