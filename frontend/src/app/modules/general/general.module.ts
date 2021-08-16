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


@NgModule({
  declarations: [
    GeneralComponent,
    LoadingIndicatorComponent,
    DistinctPipe,
    InternalRepresentationPipe,
    FormatTimePipe,
  ],
  imports: [
    CommonModule,
    TranslateModule,
    GeneralRoutingModule,
    HttpClientModule,
    MatProgressSpinnerModule
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
