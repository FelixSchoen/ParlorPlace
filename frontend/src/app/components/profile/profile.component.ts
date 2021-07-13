import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";
import {AuthService} from "../../services/auth.service";
import {TokenService} from "../../services/token.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public loading = true;
  public errorMessage = "";
  public user: User;

  constructor(private userService: UserService, private notificationService: NotificationService, private activatedRoute: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.errorMessage = "";

    const queryName: string = this.activatedRoute.snapshot.params["username"];

    if (queryName == undefined) {
      this.userService.getCurrentUser().subscribe(
        (user) => {
          this.router.navigate(["profile/" + user.username]).then();
        }
      );
    } else {
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

}
