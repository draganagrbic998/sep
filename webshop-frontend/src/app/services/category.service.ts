import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Category } from '../models/category';
import { StandardRestService } from './standard-rest.service';

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends StandardRestService<Category> {

  constructor(
    protected http: HttpClient
  ) {
    super(http, 'categories');
  }
}
