import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { CartListComponent } from './components/cart/cart-list/cart-list.component';
import { OrderListComponent } from './components/cart/order-list/order-list.component';
import { ProductFormComponent } from './components/product/product-form/product-form.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { Route } from './utils/route';

const routes: Routes = [
  {
    path: Route.LOGIN,
    component: LoginComponent
  },
  {
    path: Route.PRODUCTS,
    component: ProductListComponent
  },
  {
    path: `${Route.PRODUCT_FORM}/:id`,
    component: ProductFormComponent
  },
  {
    path: Route.CART,
    component: CartListComponent
  },
  {
    path: Route.ORDERS,
    component: OrderListComponent
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: Route.LOGIN
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
