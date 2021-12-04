import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Role } from '../models/role'
import { StandardRestService } from './standard-rest.service'

@Injectable({
  providedIn: 'root'
})
export class RoleService extends StandardRestService<Role> {
  constructor (protected http: HttpClient) {
    super(http, 'roles')
  }
}
