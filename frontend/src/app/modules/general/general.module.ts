import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GeneralRoutingModule } from './general-routing.module';
import { GeneralComponent } from './general.component';
import {LoadingIndicatorComponent} from "./loading-indicator/loading-indicator.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";


@NgModule({
  declarations: [
    GeneralComponent,
    LoadingIndicatorComponent,
  ],
  imports: [
    CommonModule,
    GeneralRoutingModule,
    MatProgressSpinnerModule
  ],
  exports: [
    LoadingIndicatorComponent,
  ]
})
export class GeneralModule { }
