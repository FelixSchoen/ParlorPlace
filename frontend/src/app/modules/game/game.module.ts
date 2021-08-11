import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {GameRoutingModule} from './game-routing.module';
import {GameComponent} from './game.component';
import {GameDirective} from './game.directive';
import {ReactiveFormsModule} from "@angular/forms";
import {MatChipsModule} from "@angular/material/chips";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatIconModule} from "@angular/material/icon";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MatListModule} from "@angular/material/list";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatTabsModule} from "@angular/material/tabs";
import {WerewolfLobbyComponent} from "../../components/lobby/werewolf-lobby/werewolf-lobby.component";
import {RoleSelectionComponent} from "../../components/lobby-components/role-selection/role-selection.component";
import {ParticipantListComponent} from "../../components/lobby-components/participant-list/participant-list.component";
import {GeneralModule} from "../general/general.module";
import {GameInterfaceComponent} from '../../components/game-interface/game-interface.component';
import {WerewolfInterfaceComponent} from '../../components/game-interface/werewolf-interface/werewolf-interface.component';
import {WerewolfPlayerListComponent} from '../../components/game-interface/werewolf-interface/werewolf-player-list/werewolf-player-list.component';
import {MatBadgeModule} from "@angular/material/badge";


@NgModule({
  declarations: [
    GameDirective,
    GameComponent,
    WerewolfLobbyComponent,
    RoleSelectionComponent,
    ParticipantListComponent,
    GameInterfaceComponent,
    WerewolfInterfaceComponent,
    WerewolfPlayerListComponent,
  ],
  imports: [
    CommonModule,
    GeneralModule,
    GameRoutingModule,
    ReactiveFormsModule,
    MatChipsModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    MatAutocompleteModule,
    MatIconModule,
    DragDropModule,
    MatListModule,
    MatProgressBarModule,
    MatTabsModule,
    MatBadgeModule,
  ]
})
export class GameModule {
}
