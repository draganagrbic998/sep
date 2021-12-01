import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './utils/auth.interceptor';
import { LoginComponent } from './components/auth/login/login.component';
import { FormComponent } from './components/utils/form/form.component';
import { SpinnerComponent } from './components/utils/spinner/spinner.component';
import { ToolbarComponent } from './components/utils/toolbar/toolbar.component';
import { ProductFormComponent } from './components/product/product-form/product-form.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { DeleteConfirmationComponent } from './components/utils/delete-confirmation/delete-confirmation.component';
import { CartListComponent } from './components/cart/cart-list/cart-list.component';
import { ProductItemComponent } from './components/product/product-item/product-item.component';
import { OrderListComponent } from './components/cart/order-list/order-list.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FormComponent,
    SpinnerComponent,
    ToolbarComponent,
    ProductFormComponent,
    ProductListComponent,
    DeleteConfirmationComponent,
    CartListComponent,
    ProductItemComponent,
    OrderListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
