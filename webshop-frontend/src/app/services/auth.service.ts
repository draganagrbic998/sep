import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Auth } from '../models/auth';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient
  ) { }

  private readonly AUTH_API = `${environment.apiUrl}/auth`;

  login(auth: Auth) {
    return this.http.post<Auth>(this.AUTH_API, auth);
  }

}
