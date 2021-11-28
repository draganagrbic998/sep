import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Currency } from '../models/currency';
import { StandardRestService } from './standard-rest.service';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService extends StandardRestService<Currency> {

  constructor(
    protected http: HttpClient
  ) {
    super(http, 'currencies');
  }
}
