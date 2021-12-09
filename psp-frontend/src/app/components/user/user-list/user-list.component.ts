import { Component } from '@angular/core'
import { UserService } from 'src/app/services/user.service'
import { Route } from 'src/app/utils/route'

@Component({
  selector: 'app-user-list',
  template: `<app-list [config]="this"></app-list>`
})
export class UserListComponent {
  constructor(public service: UserService) { }

  editRoute = Route.USER_FORM
  hideEdit = true;

  columnMappings: { [key: string]: string } = {
    email: 'User Email',
    role: 'User Role'
  }

}
