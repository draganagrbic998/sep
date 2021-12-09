import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { User } from '../models/user'
import { StandardRestService } from './standard-rest.service'

@Injectable({
  providedIn: 'root'
})
export class UserService extends StandardRestService<User> {
  constructor(protected http: HttpClient) {
    super(http, 'users')
  }
}
