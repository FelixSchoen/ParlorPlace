import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ProfileRoutingModule} from './profile-routing.module';
import {
  DialogContentProfileEditDialog,
  DialogContentProfileEnterGameDialog,
  ProfileComponent
} from "./profile.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatChipsModule} from "@angular/material/chips";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";
import {GeneralModule} from "../general/general.module";
import {MatListModule} from "@angular/material/list";


@NgModule({
  declarations: [
    ProfileComponent,
    DialogContentProfileEditDialog,
    DialogContentProfileEnterGameDialog,
  ],
  imports: [
    CommonModule,
    GeneralModule,
    ProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    MatAutocompleteModule,
    MatIconModule,
    MatMenuModule,
    MatChipsModule,
    MatListModule,
  ],
  entryComponents: [DialogContentProfileEditDialog],
})
export class ProfileModule {
}
