import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { LoginComponent } from './components/auth/login/login.component'
import { PaymentMethodFormComponent } from './components/payment-method/payment-method-form/payment-method-form.component'
import { PaymentMethodListComponent } from './components/payment-method/payment-method-list/payment-method-list.component'
import { UserFormComponent } from './components/user/user-form/user-form.component'
import { UserListComponent } from './components/user/user-list/user-list.component'
import { WebshopListComponent } from './components/webshop/webshop-list/webshop-list.component'
import { Route } from './utils/route'

const routes: Routes = [
  {
    path: Route.LOGIN,
    component: LoginComponent
  },
  {
    path: Route.WEBSHOPS,
    component: WebshopListComponent
  },
  {
    path: Route.PAYMENT_METHODS,
    component: PaymentMethodListComponent
  },
  {
    path: `${Route.PAYMENT_METHOD_FORM}/:id`,
    component: PaymentMethodFormComponent
  },
  {
    path: Route.USERS,
    component: UserListComponent
  },
  {
    path: `${Route.USER_FORM}/:id`,
    component: UserFormComponent
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: Route.LOGIN
  }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
