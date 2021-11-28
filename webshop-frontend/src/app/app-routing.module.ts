import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { ProductFormComponent } from './components/product/product-form/product-form.component';
import { Route } from './utils/route';

const routes: Routes = [
  {
    path: Route.LOGIN,
    component: LoginComponent
  },
  {
    path: `${Route.PRODUCT_FORM}/:id`,
    component: ProductFormComponent
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
