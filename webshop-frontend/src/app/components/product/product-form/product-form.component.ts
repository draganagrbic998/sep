import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/operators';
import { CategoryService } from 'src/app/services/category.service';
import { CurrencyService } from 'src/app/services/currency.service';
import { ProductService } from 'src/app/services/product.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-product-form',
  template: `<app-form [config]="this"></app-form>`
})
export class ProductFormComponent {

  constructor(
    public service: ProductService,
    private categoryService: CategoryService,
    private currencyService: CurrencyService,
    private route: ActivatedRoute,
  ) { }

  entity = 'Product'
  listRoute = Route.PRODUCTS
  categories: string[]
  currencies: string[]

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
      options: this.categoryService.read().pipe(map(res => res.map(category => category.name)))
    },
    currency: {
      type: 'select',
      validation: 'required',
      options: this.currencyService.read().pipe(map(res => res.map(currency => currency.name)))
    },
    image: {
      type: 'file',
      validation: !!+this.route.snapshot.params.id ? 'none' : 'required'
    }
  }
  style: FormStyle = {
    width: '550px',
    'margin-top': '100px'
  }

}
