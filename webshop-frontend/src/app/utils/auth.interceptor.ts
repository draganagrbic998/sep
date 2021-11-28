import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from '../services/storage.service';
import { Router } from '@angular/router';
import { Route } from './route';
import { tap } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(
        private storageService: StorageService,
        private router: Router
    ) { }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        const token = this.storageService.getAuth()?.token;

        if (token) {
            request = request.clone({
                setHeaders: {
                    Authorization: token
                }
            });
        }

        return next.handle(request).pipe(tap(() => { },
            err => {
                if (err instanceof HttpErrorResponse) {
                    if (err.status === 401 || err.status === 403) {
                        this.storageService.removeAuth();
                        this.router.navigate([Route.LOGIN]);
                    }
                }
            }));

    }
}
