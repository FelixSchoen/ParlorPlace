import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";

import {authInterceptorProviders} from './interceptors/auth.interceptor';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {OverlayContainer} from "@angular/cdk/overlay";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatSnackBarModule,
  ],
  providers: [authInterceptorProviders],
  exports: [],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor(private overlayContainer: OverlayContainer) {
  }

  changeTheme(
    theme: 'light-theme' | 'dark-theme'
  ): void {
    const overlayContainerClasses = this.overlayContainer.getContainerElement().classList;

    const themeClassesToRemove = Array.from(overlayContainerClasses)
      .filter((item: string) => item.includes('-theme'));
    if (themeClassesToRemove.length) {
      overlayContainerClasses.remove(...themeClassesToRemove);
    }

    overlayContainerClasses.add(theme);
  }

}
