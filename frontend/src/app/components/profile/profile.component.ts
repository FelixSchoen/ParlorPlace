import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public loading = true;
  public errorMessage = "";
  public user: User;

  constructor(private userService: UserService, private notificationService: NotificationService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.errorMessage = "";

    const queryName: string = this.activatedRoute.snapshot.params["username"];

    if (queryName == undefined) console.log("now dasd")

    this.activatedRoute.params.subscribe(
      params => {
        this.userService.getUserByUsername(params["username"]).subscribe(
          (user: User) => {
            this.user = user
          },
          (error) => {
            this.errorMessage = error.error;
          }).add(() => this.loading = false)
      });
  }

}
