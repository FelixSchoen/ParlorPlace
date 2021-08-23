import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LibraryRoutingModule} from './library-routing.module';
import {GeneralModule} from "../general/general.module";
import {LibraryComponent} from './library.component';
import {GameModule} from "../game/game.module";
import {WerewolfLibraryComponent} from '../../components/game-library/werewolf-library/werewolf-library.component';
import {MatTabsModule} from "@angular/material/tabs";
import {MatIconModule} from "@angular/material/icon";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";


@NgModule({
  declarations: [

    LibraryComponent,
     WerewolfLibraryComponent,
  ],
  imports: [
    CommonModule,
    GeneralModule,
    LibraryRoutingModule,
    GameModule,
    MatTabsModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule,
  ]
})
export class LibraryModule {
}
