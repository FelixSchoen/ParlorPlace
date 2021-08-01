import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GameRoutingModule } from './game-routing.module';
import { GameComponent } from './game.component';
import { GameDirective } from './game.directive';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
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
    GameRoutingModule,
    //
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
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
    GeneralModule,
  ]
})
export class GameModule { }
