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
import {LobbyComponent} from "../lobby/lobby.component";
import {WerewolfLobbyComponent} from "../lobby/werewolf-lobby/werewolf-lobby.component";
import {RoleSelectionComponent} from "../../components/lobby/role-selection/role-selection.component";
import {PlayerListComponent} from "../../components/lobby/player-list/player-list.component";
import {GeneralModule} from "../general/general.module";


@NgModule({
  declarations: [
    GameComponent,
    GameDirective,
    LobbyComponent,
    WerewolfLobbyComponent,
    RoleSelectionComponent,
    PlayerListComponent,
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
  ]
})
export class GameModule { }
