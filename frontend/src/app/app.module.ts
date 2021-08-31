import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {TranslateCompiler, TranslateLoader, TranslateModule, TranslateService} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {HttpClient, HttpClientModule} from '@angular/common/http';

import {authInterceptorProviders} from './interceptors/auth.interceptor';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {OverlayContainer} from "@angular/cdk/overlay";
import {TranslateMessageFormatCompiler} from "ngx-translate-messageformat-compiler";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      },
      compiler: {
        provide: TranslateCompiler,
        useClass: TranslateMessageFormatCompiler
      }
    }),
    MatSnackBarModule,
  ],
  providers: [authInterceptorProviders,{
    provide: LocationStrategy,
    useClass: HashLocationStrategy
  }],
  exports: [],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor(private overlayContainer: OverlayContainer, private translateService: TranslateService) {
    translateService.setDefaultLang("en");
    translateService.use("en");
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

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}
