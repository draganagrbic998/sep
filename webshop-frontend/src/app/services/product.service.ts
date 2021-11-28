import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../models/page';
import { Product, ProductUpload } from '../models/product';
import { StandardRestService } from './standard-rest.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService extends StandardRestService<Product> {

  constructor(
    protected http: HttpClient
  ) {
    super(http, 'products');
  }

  readPaged(category: string, page: number) {
    return this.http.get<Page<Product>>(`${this.API_URL}?category=${category}&page=${page}&size=10`);
  }

  upload(upload: ProductUpload) {
    const data = new FormData();
    for (const field in upload) {
      if (field === 'id' && !upload[field]) {
        continue;
      }
      data.append(field, upload[field]);
    }
    return this.http.post<Product>(this.API_URL, data);
  }


}
