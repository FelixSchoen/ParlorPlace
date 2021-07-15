import {Component, OnInit} from '@angular/core';
import {NotificationService} from "../../services/notification.service";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.css']
})
export class EntryComponent {

  constructor(public appComponent: AppComponent) {
  }

  changeMode() {
    this.appComponent.switchMode();
  }

}
