import { Component, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { Product } from 'src/app/models/product';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { Route } from 'src/app/utils/route';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService
  ) { }

  products: Product[]
  categories: string[]
  category: string;
  page = 0
  productFormLink = '/' + Route.PRODUCT_FORM + '/new'

  ngOnInit() {
    this.readCategories().then(() => this.readProducts())
  }

  async readProducts() {
    this.products = await this.productService.readPaged(this.category, this.page).pipe(map(res => res.content)).toPromise();
  }

  private async readCategories() {
    this.categories = await this.categoryService.read().pipe(map(res => res.map(category => category.name))).toPromise();
    this.categories = ['Personal', 'All', ...this.categories]
  }

}
