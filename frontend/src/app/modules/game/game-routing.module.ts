import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ProfileComponent} from "../profile/profile.component";
import {GameComponent} from "./game.component";

const routes: Routes = [
  {path: ':identifier', component: GameComponent},
  {path: '**', redirectTo: 'entry', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GameRoutingModule { }
