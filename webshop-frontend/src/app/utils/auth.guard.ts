import { Injectable } from '@angular/core'
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router'
import { StorageService } from '../services/storage.service'
import { Route } from './route'

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private storageService: StorageService,
    private router: Router
  ) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (route.data.role !== this.storageService.getAuth()?.role) {
      this.router.navigate([Route.LOGIN])
      return false
    }
    return true
  }
}
