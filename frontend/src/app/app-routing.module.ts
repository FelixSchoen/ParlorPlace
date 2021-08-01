import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {EntryComponent} from "./modules/entry/entry.component";
import {ProfileComponent} from "./modules/profile/profile.component";
import {AuthGuard} from "./authentication/auth.guard";
import {ExperimentalComponent} from "./modules/experimental/experimental.component";
import {LobbyComponent} from "./modules/lobby/lobby.component";
import {WerewolfLobbyComponent} from "./modules/lobby/werewolf-lobby/werewolf-lobby.component";

const routes: Routes = [
  {path: '', redirectTo: 'entry', pathMatch: 'full'},
  {path: 'entry', component: EntryComponent},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'profile/:username', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'game/lobby', redirectTo: 'entry', pathMatch: 'full'},
  {path: 'game/lobby/:identifier', component: LobbyComponent, canActivate: [AuthGuard]},
  {path: 'game/werewolf/:identifier/lobby', component: WerewolfLobbyComponent, canActivate: [AuthGuard]},
  {path: 'experimental', component: ExperimentalComponent},
  {path: '**', redirectTo: 'entry', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
