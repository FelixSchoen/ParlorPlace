import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EntryComponent} from './components/entry/entry.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import {authInterceptorProviders} from './interceptors/auth.interceptor';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {DialogContentSignupDialog, SignupComponent} from './components/authentication/signup/signup.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ToastrModule} from "ngx-toastr";
import {DialogContentSigninDialog, SigninComponent} from './components/authentication/signin/signin.component';
import {
  DialogContentProfileEditDialog,
  DialogContentProfileEnterGameDialog,
  ProfileComponent
} from './components/profile/profile.component';
import {LoadingIndicatorComponent} from './components/utility/loading-indicator/loading-indicator.component';
import {MatChipsModule} from "@angular/material/chips";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatOptionModule} from "@angular/material/core";
import {MatIconModule} from "@angular/material/icon";
import { ExperimentalComponent } from './components/experimental/experimental.component';
import {OverlayContainer} from "@angular/cdk/overlay";
import {MatMenuModule} from "@angular/material/menu";

@NgModule({
  declarations: [
    AppComponent,
    EntryComponent,
    SignupComponent,
    DialogContentSignupDialog,
    SigninComponent,
    DialogContentSigninDialog,
    ProfileComponent,
    DialogContentProfileEditDialog,
    DialogContentProfileEnterGameDialog,
    LoadingIndicatorComponent,
    ExperimentalComponent
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
    MatChipsModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatOptionModule,
    MatIconModule,
    MatMenuModule
  ],
  entryComponents: [DialogContentSignupDialog, DialogContentSigninDialog, DialogContentProfileEditDialog],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor(private overlayContainer: OverlayContainer) {
  }

  changeTheme(theme: 'light-theme' | 'dark-theme'): void {
    console.log(theme)
    const overlayContainerClasses = this.overlayContainer.getContainerElement().classList;

    const themeClassesToRemove = Array.from(overlayContainerClasses)
      .filter((item: string) => item.includes('-theme'));
    if (themeClassesToRemove.length) {
      overlayContainerClasses.remove(...themeClassesToRemove);
    }

    overlayContainerClasses.add(theme);
  }

}
