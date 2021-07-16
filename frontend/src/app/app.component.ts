import {Component, HostBinding} from '@angular/core';
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  public title = 'ParlorPlace';
  public darkTheme = true;


  constructor(authService: AuthService) {
  }

  public switchMode() {
    this.darkTheme = !this.darkTheme;
  }
}
