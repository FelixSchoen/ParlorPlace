import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ExperimentalRoutingModule} from './experimental-routing.module';
import {ExperimentalComponent} from "./experimental.component";
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
import {MatMenuModule} from "@angular/material/menu";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MatListModule} from "@angular/material/list";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatTabsModule} from "@angular/material/tabs";
import {MatTableModule} from "@angular/material/table";


@NgModule({
  declarations: [
    ExperimentalComponent,
  ],
    imports: [
        CommonModule,
        ExperimentalRoutingModule,
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
        MatMenuModule,
        DragDropModule,
        MatListModule,
        MatProgressBarModule,
        MatTabsModule,
        MatTableModule,
    ]
})
export class ExperimentalModule {
}
