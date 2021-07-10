import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EntryComponent } from './components/entry/entry.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import { authInterceptorProviders } from './interceptors/auth.interceptor';
import { AppRoutingModule } from './app-routing.module';
import {AppComponent} from "./app.component";

@NgModule({
  declarations: [
    AppComponent,
    EntryComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
