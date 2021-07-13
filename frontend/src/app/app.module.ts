import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EntryComponent} from './components/entry/entry.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import {authInterceptorProviders} from './interceptors/auth.interceptor';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {SignupComponent} from './components/authentication/signup/signup.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ToastrModule} from "ngx-toastr";
import {ToastComponent} from './components/toast/toast.component';
import {SigninComponent} from './components/authentication/signin/signin.component';
import {ProfileComponent} from './components/profile/profile.component';
import { LoadingIndicatorComponent } from './components/utility/loading-indicator/loading-indicator.component';

@NgModule({
  declarations: [
    AppComponent,
    EntryComponent,
    SignupComponent,
    ToastComponent,
    SigninComponent,
    ProfileComponent,
    LoadingIndicatorComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule,
    ToastrModule.forRoot({positionClass: 'toast-bottom-left'}),
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
