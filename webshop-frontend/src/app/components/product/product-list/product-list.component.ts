import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map } from 'rxjs/operators';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/services/cart.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { DIALOG_CONFIG, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';
import { Route } from 'src/app/utils/route';
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
    private cartService: CartService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  products: Product[]
  categories: string[]
  category: string;
  page = 0

  ngOnInit() {
    this.readCategories().then(() => this.readProducts())
  }

  cartPendingId: number = null;

  async addToCart(product: Product) {
    this.cartPendingId = product.id;
    try {
      await this.cartService.addToCart(product.id).toPromise();
      this.cartPendingId = null;
      this.snackbar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);
    } catch {
      this.cartPendingId = null;
      this.snackbar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);
    }
  }

  edit(product: Product) {
    return `/${Route.PRODUCT_FORM}/${product.id}`
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
