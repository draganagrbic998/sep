import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { LoginComponent } from './components/auth/login/login.component'
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
    path: '**',
    pathMatch: 'full',
    redirectTo: Route.LOGIN
  }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
