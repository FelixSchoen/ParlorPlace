import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LibraryComponent} from "./library.component";

const routes: Routes = [
  {path: ':identifier', component: LibraryComponent, runGuardsAndResolvers: "always"},
  {path: '**', redirectTo: 'profile', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LibraryRoutingModule {
}
