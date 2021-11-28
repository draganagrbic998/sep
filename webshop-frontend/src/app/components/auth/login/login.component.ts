import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Auth, Role } from 'src/app/models/auth';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_TEXT, SNACKBAR_ERROR_CONFIG } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-login',
  template: `<app-form title="Log In" [config]="config" [pending]="pending" [style]="style" (submit)="login($event)"></app-form>`
})
export class LoginComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router,
    private snackbar: MatSnackBar,
  ) { }

  pending = false;
  config: FormConfig = {
    email: {
      validation: 'required'
    },
    password: {
      type: 'password',
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '500px',
    'margin-top': '250px'
  }

  ngOnInit() {
    this.storageService.removeAuth();
  }

  async login(auth: Auth) {
    this.pending = true;

    try {
      const res = await this.authService.login(auth).toPromise();
      this.pending = false;
      this.storageService.setAuth(res);

      this.router.navigate([`${Route.PRODUCT_FORM}/new`])
    }

    catch {
      this.pending = false;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

}
