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
import { DeleteConfirmationComponent } from './components/utils/delete-confirmation/delete-confirmation.component';
import { WebshopListComponent } from './components/webshop/webshop-list/webshop-list.component';
import { ListComponent } from './components/utils/list/list.component';
import { WebshopFormComponent } from './components/webshop/webshop-form/webshop-form.component';
import { PaymentMethodListComponent } from './components/payment-method/payment-method-list/payment-method-list.component';
import { PaymentMethodFormComponent } from './components/payment-method/payment-method-form/payment-method-form.component';
import { UserFormComponent } from './components/user/user-form/user-form.component';
import { UserListComponent } from './components/user/user-list/user-list.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FormComponent,
    SpinnerComponent,
    ToolbarComponent,
    DeleteConfirmationComponent,
    WebshopListComponent,
    ListComponent,
    WebshopFormComponent,
    PaymentMethodListComponent,
    PaymentMethodFormComponent,
    UserFormComponent,
    UserListComponent
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
