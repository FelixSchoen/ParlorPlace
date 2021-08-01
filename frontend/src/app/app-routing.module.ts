import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {EntryComponent} from "./modules/entry/entry.component";
import {AuthGuard} from "./authentication/auth.guard";
import {ExperimentalComponent} from "./modules/experimental/experimental.component";
import {LobbyComponent} from "./modules/lobby/lobby.component";
import {WerewolfLobbyComponent} from "./modules/lobby/werewolf-lobby/werewolf-lobby.component";

const routes: Routes = [
  {path: '', redirectTo: 'entry', pathMatch: 'full'},
  {path: 'entry', loadChildren: () => import("./modules/entry/entry.module").then(m => m.EntryModule)},
  {path: 'profile', loadChildren: () => import("./modules/profile/profile.module").then(m => m.ProfileModule), canActivate: [AuthGuard]},
  {path: 'game', loadChildren: () => import("./modules/game/game.module").then(m => m.GameModule), canActivate: [AuthGuard]},
  {path: 'experimental', loadChildren: () => import("./modules/experimental/experimental.module").then(m => m.ExperimentalModule)},
  {path: '**', redirectTo: 'entry', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
