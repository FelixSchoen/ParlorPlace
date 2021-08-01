import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {EntryRoutingModule} from './entry-routing.module';
import {DialogContentSignupDialog, SignupComponent} from "../../components/authentication/signup/signup.component";
import {DialogContentSigninDialog, SigninComponent} from "../../components/authentication/signin/signin.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatChipsModule} from "@angular/material/chips";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatIconModule} from "@angular/material/icon";
import {EntryComponent} from "./entry.component";


@NgModule({
  declarations: [
    EntryComponent,
    SignupComponent,
    DialogContentSignupDialog,
    SigninComponent,
    DialogContentSigninDialog,
  ],
  imports: [
    CommonModule,
    EntryRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatChipsModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatIconModule,
  ],
  entryComponents: [DialogContentSignupDialog, DialogContentSigninDialog,],
})
export class EntryModule {
}
