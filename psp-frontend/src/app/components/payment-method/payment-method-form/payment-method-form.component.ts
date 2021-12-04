import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { PaymentMethod } from 'src/app/models/payment-method';
import { PaymentMethodService } from 'src/app/services/payment-method.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-payment-method-form',
  template: `<app-form [config]="this"></app-form>`
})
export class PaymentMethodFormComponent implements OnInit {

  constructor(
    private paymentMethodService: PaymentMethodService,
    private router: Router,
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
  ) { }

  readFunction: () => Observable<PaymentMethod>;
  title = "Create Payment Method"

  pending = false;
  formConfig: FormConfig = {
    name: {
      validation: 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

  get paymentMethodId() {
    return +this.route.snapshot.params.id
  }

  ngOnInit() {
    if (this.paymentMethodId) {
      this.readFunction = () => this.paymentMethodService.readOne(this.paymentMethodId)
      this.title = "Edit Payment Method"
    }
  }

  async save(paymentMethod: PaymentMethod) {
    if (this.paymentMethodId) {
      paymentMethod.id = this.paymentMethodId;
    }
    this.pending = true;

    try {
      await this.paymentMethodService.save(paymentMethod).toPromise();
      this.pending = false;
      this.snackbar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);
      this.router.navigate([Route.PAYMENT_METHODS]);
    }

    catch {
      this.pending = false;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

}
