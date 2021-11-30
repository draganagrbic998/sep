import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { map } from 'rxjs/operators';
import { Product } from 'src/app/models/product';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { DIALOG_CONFIG } from 'src/app/utils/popup';
import { DeleteConfirmationComponent } from '../../utils/delete-confirmation/delete-confirmation.component';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private dialog: MatDialog
  ) { }

  products: Product[]
  categories: string[]
  category: string;
  page = 0

  ngOnInit() {
    this.readCategories().then(() => this.readProducts())
  }

  edit(product: Product) {

  }

  async delete(product: Product) {
    const options: MatDialogConfig = { ...DIALOG_CONFIG, ...{ data: () => this.productService.delete(product.id) } };
    const res = await this.dialog.open(DeleteConfirmationComponent, options).afterClosed().toPromise()
    if (res) {
      this.readProducts()
    }
  }

  async readProducts() {
    this.products = await this.productService.readPaged(this.category, this.page).pipe(map(res => res.content)).toPromise();

  }

  private async readCategories() {
    this.categories = await this.categoryService.read().pipe(map(res => res.map(category => category.name))).toPromise();
    this.categories = ['Personal', 'All', ...this.categories]
  }

}
