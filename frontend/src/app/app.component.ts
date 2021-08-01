import {Component, HostBinding, OnInit} from '@angular/core';
import {AuthService} from "./authentication/auth.service";
import {AppModule} from "./app.module";
import {getAppModulePath} from "@angular/cdk/schematics";

const THEME_KEY = 'theme-style';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public title = 'ParlorPlace';
  public darkTheme = false;

  constructor(private appModule: AppModule) {
  }

  ngOnInit(): void {
    if (!(!!localStorage.getItem(THEME_KEY))) {
      this.darkTheme = false;
      this.appModule.changeTheme('light-theme');
    }
    else if (localStorage.getItem(THEME_KEY) == "dark") {
      this.darkTheme = true;
      this.appModule.changeTheme('dark-theme');
    }
    else if (localStorage.getItem(THEME_KEY) == "light") {
      this.darkTheme = false;
      this.appModule.changeTheme('light-theme');
    }
  }

  public switchMode() {
    this.darkTheme = !this.darkTheme;

    if (this.darkTheme) {
      localStorage.setItem(THEME_KEY, "dark");
      this.appModule.changeTheme('dark-theme');
    } else {
      localStorage.setItem(THEME_KEY, "light");
      this.appModule.changeTheme('light-theme');
    }

  }

}
