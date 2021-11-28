import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-application-upload',
  template: `<app-form title="Upload Application" [config]="config" [pending]="pending" [style]="style" (submit)="upload($event)"></app-form>`
})
export class ProductFormComponent {

  constructor(
    private applicationService: ApplicationService,
    private router: Router,
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
  ) { }

  pending = false;
  config: FormConfig = {
    firstName: {
      validation: 'required'
    },
    lastName: {
      validation: 'required'
    },
    email: {
      validation: 'required'
    },
    address: {
      type: 'location',
      validation: 'required'
    },
    education: {
      validation: 'required'
    },
    cvFile: {
      type: 'file',
      validation: 'required'
    },
    letterFile: {
      type: 'file',
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

  async upload(upload: ApplicationUpload) {
    upload.advertisementId = +this.route.snapshot.params.advertisementId
    this.pending = true;

    try {
      await this.applicationService.upload(upload).toPromise();
      this.pending = false;
      this.snackbar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);
      this.router.navigate([Route.ADVERTISEMENTS]);
    }

    catch {
      this.pending = false;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

}
