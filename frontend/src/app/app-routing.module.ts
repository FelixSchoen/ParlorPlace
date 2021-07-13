import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {EntryComponent} from "./components/entry/entry.component";
import {ProfileComponent} from "./components/profile/profile.component";

const routes: Routes = [
  {path: '', redirectTo: 'entry', pathMatch: 'full'},
  {path: 'entry', component: EntryComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'profile/:username', component: ProfileComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
