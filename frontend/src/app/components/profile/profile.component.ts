import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";
import {TokenService} from "../../services/token.service";
import {FormControl} from "@angular/forms";
import {catchError, debounceTime, distinctUntilChanged, switchMap} from "rxjs/operators";
import {Observable, of} from "rxjs";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public profileUser: User;
  public currentUser: User;

  public loading: boolean;
  public errorMessage: string;

  public userSearchControl: FormControl = new FormControl();
  public filteredOptions: Observable<User[]>;

  constructor(private userService: UserService, private tokenService: TokenService, private notificationService: NotificationService,
              private activatedRoute: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

    this.loading = true;
    this.errorMessage = "";

    const queryName: string = this.activatedRoute.snapshot.params["username"];
    const updated = this.activatedRoute.snapshot.queryParamMap.get("updated");

    if (updated === "1") {
      this.notificationService.showSuccess("Updated user");
    }

    this.filteredOptions = this.userSearchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => term.length < 3 ? [] : this.userService.getAllUsersFiltered(term, term).pipe(
        catchError(() => {
          return of([]);
        })
      ))
    )

    this.userService.getCurrentUser().subscribe(
      (user) => {
        this.currentUser = user;
        if (queryName == undefined) {
          this.router.navigate(["profile/" + user.username]).then();
        } else {
          this.userService.getUserByUsername(queryName).subscribe(
            (user: User) => {
              this.profileUser = user
            },
            (error) => {
              this.errorMessage = error.error;
            }).add(() => this.loading = false)
        }
      }
    );
  }

  displayUser(user: User): string {
    return user ? user.nickname + " (" + user.username + ")" : "";
  }

  onSelect($event: any) {
    const selectedUser: User = $event.source.value
    this.router.navigate(["profile/"+selectedUser.username]).then();
  }

  signOut() {
    this.tokenService.signout();
    this.notificationService.showSuccess("Signed out");
    this.router.navigate([""]).then();
  }

}
