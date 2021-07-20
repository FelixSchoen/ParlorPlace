import {Component, HostBinding} from '@angular/core';
import {AuthService} from "./services/auth.service";
import {AppModule} from "./app.module";
import {getAppModulePath} from "@angular/cdk/schematics";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  public title = 'ParlorPlace';
  public darkTheme = false;


  constructor(private appModule: AppModule) {
  }

  public switchMode() {
    this.darkTheme = !this.darkTheme;

    if (this.darkTheme) {
      this.appModule.changeTheme('dark-theme');
    } else {
      this.appModule.changeTheme('light-theme');
    }

  }

}
