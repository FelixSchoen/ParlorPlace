import {Component, OnInit} from '@angular/core';
import {AppModule} from "./app.module";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";

const THEME_KEY = 'theme-style';

const solid_folder = "assets/icon/solid/"
const duotone_folder = "assets/icon/duotone/"

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public title = 'ParlorPlace';
  public darkTheme = false;

  constructor(
    private appModule: AppModule,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {

    // General

    {
      this.matIconRegistry.addSvgIcon(
        "duotone-unknown",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "question.svg")
      );
      this.matIconRegistry.addSvgIcon(
        "duotone-info",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "info-circle.svg")
      );
    }

    // Overlapping

    {
      this.matIconRegistry.addSvgIcon(
        "duotone-bed",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "bed.svg")
      );

      this.matIconRegistry.addSvgIcon(
        "duotone-death",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "tombstone.svg")
      );
    }

    // Werewolf

    {
      this.matIconRegistry.addSvgIcon(
        "duotone-werewolf",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "claw-marks.svg")
      );
      this.matIconRegistry.addSvgIcon(
        "duotone-villager",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "utensil-fork.svg")
      );
      this.matIconRegistry.addSvgIcon(
        "duotone-seer",
        this.domSanitizer.bypassSecurityTrustResourceUrl(duotone_folder + "eye.svg")
      );
    }

  }


  ngOnInit(): void {
    if (!(!!localStorage.getItem(THEME_KEY))) {
      this.darkTheme = false;
      this.appModule.changeTheme('light-theme');
    } else if (localStorage.getItem(THEME_KEY) == "dark") {
      this.darkTheme = true;
      this.appModule.changeTheme('dark-theme');
    } else if (localStorage.getItem(THEME_KEY) == "light") {
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
