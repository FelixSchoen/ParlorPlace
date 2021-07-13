import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";
import {AuthService} from "../../services/auth.service";
import {TokenService} from "../../services/token.service";
import {Observable, of, OperatorFunction} from "rxjs";
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public currentUser: User;

  public loading = true;
  public errorMessage = "";
  public user: User;

  selectUserTypeahead: any;

  constructor(private userService: UserService, private tokenService: TokenService, private notificationService: NotificationService, private activatedRoute: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.errorMessage = "";

    const queryName: string = this.activatedRoute.snapshot.params["username"];

    this.userService.getCurrentUser().subscribe(
      (user) => {
        this.currentUser = user;
        console.log(this.userService.isAdmin(this.currentUser))
        console.log(this.currentUser)
        if (queryName == undefined) {
          this.router.navigate(["profile/" + user.username]).then();
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
    );


  }

  search: OperatorFunction<string, readonly User[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => term.length < 3 ? [] : this.userService.getAllUsersFiltered(term, term).pipe(
        catchError(() => {
          return of([]);
        }))
      ),
    )

  formatter = (user: User) => user.nickname + " (" + user.username + ")";

  onSelect($event: any, user: User) {
    $event.preventDefault();
    this.selectUserTypeahead = null;
    this.router.navigate(["profile/" + user.username]).then();
  }

  signOut() {
    this.tokenService.signout();
    this.router.navigate([""]).then();
  }

}
