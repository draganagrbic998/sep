import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product, ProductUpload } from 'src/app/models/product';
import { CategoryService } from 'src/app/services/category.service';
import { CurrencyService } from 'src/app/services/currency.service';
import { ProductService } from 'src/app/services/product.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-application-upload',
  template: `<app-form [config]="this"></app-form>`
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

  readFunction: () => Observable<Product>;
  title = "Create Product"
  categories: string[];
  currencies: string[];
  pending = false;
  formConfig: FormConfig = {
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
      // if api failed, return empty list
      options: this.categoryService.read().pipe(map(res => res.map(category => category.name)))
    },
    currency: {
      type: 'select',
      validation: 'required',
      options: this.currencyService.read().pipe(map(res => res.map(currency => currency.name)))
    },
    image: {
      type: 'file',
      validation: !this.productId ? 'required' : 'none'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

  get productId() {
    return +this.route.snapshot.params.id
  }

  ngOnInit() {
    if (this.productId) {
      this.readFunction = () => this.productService.readOne(this.productId)
      this.title = "Edit Product"
    }
  }

  async save(upload: ProductUpload) {
    if (this.productId) {
      upload.id = this.productId;
    }
    this.pending = true;

    try {
      await this.productService.upload(upload).toPromise();
      this.pending = false;
      this.snackbar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);
      this.router.navigate([Route.PRODUCTS]);
    }

    catch {
      this.pending = false;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

}
