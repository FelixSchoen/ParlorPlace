import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {GeneralRoutingModule} from './general-routing.module';
import {GeneralComponent} from './general.component';
import {LoadingIndicatorComponent} from "./loading-indicator/loading-indicator.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {DistinctPipe} from "../../pipes/distinct.pipe";
import {HttpClientModule} from "@angular/common/http";
import {InternalRepresentationPipe} from "../../pipes/internal-representation.pipe";
import {TranslateModule} from "@ngx-translate/core";
import {FormatTimePipe} from "../../pipes/format-time.pipe";
import {DialogContentInfoDialog} from './info-dialog/dialog-content-info-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";


@NgModule({
  declarations: [
    GeneralComponent,
    LoadingIndicatorComponent,
    DistinctPipe,
    InternalRepresentationPipe,
    FormatTimePipe,
    DialogContentInfoDialog,
  ],
  imports: [
    CommonModule,
    TranslateModule,
    GeneralRoutingModule,
    HttpClientModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatIconModule,
    MatButtonModule
  ],
  exports: [
    TranslateModule,
    LoadingIndicatorComponent,
    DistinctPipe,
    InternalRepresentationPipe,
    FormatTimePipe,
  ]
})
export class GeneralModule { }
