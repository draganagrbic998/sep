import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { WebShop } from 'src/app/models/webshop';
import { WebshopService } from 'src/app/services/webshop.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-application-upload',
  template: `<app-form [config]="this"></app-form>`
})
export class WebshopFormComponent implements OnInit {

  constructor(
    private webshopService: WebshopService,
    private router: Router,
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
  ) { }

  readFunction: () => Observable<WebShop>;
  title = "Create Webshop"

  pending = false;
  formConfig: FormConfig = {
    name: {
      validation: 'required'
    },
    url: {
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

  get webshopId() {
    return +this.route.snapshot.params.id
  }

  ngOnInit() {
    if (this.webshopId) {
      this.readFunction = () => this.webshopService.readOne(this.webshopId)
      this.title = "Edit Webshop"
    }
  }

  async save(webshop: WebShop) {
    if (this.webshopId) {
      webshop.id = this.webshopId;
    }
    this.pending = true;

    try {
      await this.webshopService.save(webshop).toPromise();
      this.pending = false;
      this.snackbar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);
      this.router.navigate([Route.WEBSHOPS]);
    }

    catch {
      this.pending = false;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

}
