import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { ProductUpload } from 'src/app/models/product';
import { CategoryService } from 'src/app/services/category.service';
import { CurrencyService } from 'src/app/services/currency.service';
import { ProductService } from 'src/app/services/product.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-application-upload',
  template: `<app-form title="Upload Product" [config]="config" [pending]="pending" [style]="style" (submit)="upload($event)"></app-form>`
})
export class ProductFormComponent implements OnInit {

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private currencyService: CurrencyService,
    private router: Router,
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
  ) { }

  categories: string[];
  currencies: string[];
  pending = false;
  config: FormConfig;
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

  ngOnInit() {
    this.readOptions().then(() => this.setFormConfig())
      .catch(() => this.setFormConfig())
  }

  private async readOptions() {
    this.categories = await this.categoryService.read().pipe(map(res => res.map(category => category.name))).toPromise()
    this.currencies = await this.currencyService.read().pipe(map(res => res.map(currency => currency.name))).toPromise()
  }

  private setFormConfig() {
    this.config = {
      name: {
        validation: 'required'
      },
      description: {
        validation: 'required'
      },
      price: {
        validation: 'price'
      },
      category: {
        type: 'select',
        validation: 'required',
        options: this.categories
      },
      currency: {
        type: 'select',
        validation: 'required',
        options: this.currencies
      },
      image: {
        type: 'file',
        validation: 'required'
      }
    }
  }

  async upload(upload: ProductUpload) {
    this.pending = true;

    try {
      await this.productService.upload(upload).toPromise();
      this.pending = false;
      this.snackbar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);
      alert('AHA');
      // this.router.navigate([Route.ADVERTISEMENTS]);
    }

    catch {
      this.pending = false;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

}
