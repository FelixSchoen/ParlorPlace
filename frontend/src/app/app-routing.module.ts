import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {AuthGuard} from "./authentication/auth.guard";

const routes: Routes = [
  {path: '', redirectTo: 'entry', pathMatch: 'full'},
  {path: 'entry', loadChildren: () => import("./modules/entry/entry.module").then(m => m.EntryModule)},
  {path: 'profile', loadChildren: () => import("./modules/profile/profile.module").then(m => m.ProfileModule), canActivate: [AuthGuard]},
  {path: 'game', loadChildren: () => import("./modules/game/game.module").then(m => m.GameModule), canActivate: [AuthGuard]},
  {path: 'experimental', loadChildren: () => import("./modules/experimental/experimental.module").then(m => m.ExperimentalModule)},
  {path: '**', redirectTo: 'profile', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
