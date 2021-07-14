import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User, UserUpdateRequest} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";
import {AuthService} from "../../services/auth.service";
import {TokenService} from "../../services/token.service";
import {Observable, of, OperatorFunction} from "rxjs";
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import Validation from "../../validators/Validation";

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

  form: FormGroup
  submitted = false;
  orderObj: any;

  selectUserTypeahead: any;

  constructor(private userService: UserService, private tokenService: TokenService, private notificationService: NotificationService,
              private activatedRoute: ActivatedRoute, private router: Router, private formBuilder: FormBuilder, private modalService: NgbModal) {
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

    this.userService.getCurrentUser().subscribe(
      (user) => {
        this.currentUser = user;
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

    this.form = this.formBuilder.group({
      // username: ["", [Validators.minLength(3), Validators.maxLength(15)]],
      nickname: ["", [Validators.minLength(3), Validators.maxLength(15)]],
      password: ["", [Validators.minLength(8), Validators.maxLength(255)]],
      confirmPassword: [""],
      email: ["", [Validators.email]]
    }, {
      validators: [Validation.match("password", "confirmPassword")]
    })
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

  open(content: any) {
    this.modalService.open(content, {centered: true})
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.form!.invalid) {
      return;
    } else {
      const userUpdateRequest: UserUpdateRequest = new UserUpdateRequest(this.user.id, null, this.f.password.value,
        this.f.nickname.value, this.f.email.value, null);

      this.userService.updateUser(this.user.id, userUpdateRequest).subscribe(
        (updatedUser: User) => {
          this.modalService.dismissAll();
          this.router.navigate(["profile/" + updatedUser.username], {queryParams: {updated: 1}}).then();
        },
        (error) => {
          this.notificationService.showError(error.error);
        }
      )
    }
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form!.controls;
  }

}
