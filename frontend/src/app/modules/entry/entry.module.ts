import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {EntryRoutingModule} from './entry-routing.module';
import {DialogContentSignupDialog, SignupComponent} from "../../components/authentication/signup/signup.component";
import {DialogContentSigninDialog, SigninComponent} from "../../components/authentication/signin/signin.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
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
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
  ],
  entryComponents: [DialogContentSignupDialog, DialogContentSigninDialog,],
})
export class EntryModule {
}
