import {Component, OnInit} from '@angular/core';
import {NotificationService} from "../../services/notification.service";
import {AppComponent} from "../../app.component";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.css']
})
export class EntryComponent implements OnInit {

  constructor(public appComponent: AppComponent, public authService: AuthService, public router: Router) {
  }

  ngOnInit(): void {
    if (this.authService.isSignedIn()) {
      this.router.navigate(["/profile"]).then();
    }
  }

  changeMode() {
    this.appComponent.switchMode();
  }

}
