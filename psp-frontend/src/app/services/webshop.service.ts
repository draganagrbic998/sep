import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { WebShop } from '../models/webshop'
import { StandardRestService } from './standard-rest.service'

@Injectable({
  providedIn: 'root'
})
export class WebshopService extends StandardRestService<WebShop> {
  constructor(protected http: HttpClient) {
    super(http, 'webshops')
  }
}
