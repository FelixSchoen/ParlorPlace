import {Component, HostBinding} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ParlorPlace';

  public darkTheme = true;

  public switchMode() {
    this.darkTheme = !this.darkTheme;
  }
}
