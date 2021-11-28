import { Injectable } from '@angular/core';
import { Auth } from '../models/auth';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private readonly AUTH_KEY = 'AUTH';

  getAuth(): Auth {
    return JSON.parse(localStorage.getItem(this.AUTH_KEY));
  }

  setAuth(auth: Auth) {
    localStorage.setItem(this.AUTH_KEY, JSON.stringify(auth));
  }

  removeAuth() {
    localStorage.removeItem(this.AUTH_KEY);
  }

}
