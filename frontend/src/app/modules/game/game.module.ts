import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {GameRoutingModule} from './game-routing.module';
import {GameComponent} from './game.component';
import {ComponentHost} from './component-host.directive';
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
import {WerewolfLobbyComponent} from "../../components/game-lobby/werewolf-lobby/werewolf-lobby.component";
import {RoleSelectionComponent} from "../../components/game-common/lobby-components/role-selection/role-selection.component";
import {ParticipantListComponent} from "../../components/game-common/lobby-components/participant-list/participant-list.component";
import {GeneralModule} from "../general/general.module";
import {WerewolfInterfaceComponent} from '../../components/game-interface/werewolf-interface/werewolf-interface.component';
import {WerewolfPlayerListComponent} from '../../components/game-common/interface-components/werewolf-player-list/werewolf-player-list.component';
import {MatBadgeModule} from "@angular/material/badge";
import {WerewolfLogEntryListComponent} from '../../components/game-common/interface-components/log-entry-list/werewolf-log-entry-list/werewolf-log-entry-list.component';
import {WerewolfPlayerWerewolfVoteComponent} from '../../components/game-common/interface-components/vote/werewolf-player-werewolf-vote/werewolf-player-werewolf-vote.component';
import {MatTableModule} from "@angular/material/table";
import {ResourcepackSelectionComponent} from '../../components/game-common/lobby-components/resourcepack-selection/resourcepack-selection.component';
import {MatSelectModule} from "@angular/material/select";


@NgModule({
  declarations: [
    ComponentHost,
    GameComponent,
    WerewolfLobbyComponent,
    RoleSelectionComponent,
    ParticipantListComponent,
    WerewolfInterfaceComponent,
    WerewolfPlayerListComponent,
    WerewolfLogEntryListComponent,
    WerewolfPlayerWerewolfVoteComponent,
    ResourcepackSelectionComponent,
  ],
  exports: [
    ComponentHost,
    WerewolfPlayerListComponent
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
    MatTableModule,
    MatSelectModule,
  ]
})
export class GameModule {
}
