import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User, UserUpdateRequest} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";
import {TokenService} from "../../services/token.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {catchError, debounceTime, distinctUntilChanged, switchMap} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {UserRole, UserRoleUtil} from "../../enums/userrole";
import {ErrorStateMatcher} from "@angular/material/core";
import Validation, {MatchingErrorStateMatcher} from "../../validators/Validation";
import {MatChipInputEvent} from "@angular/material/chips";
import {ENTER} from "@angular/cdk/keycodes";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {Utility} from "../../utility/utility";

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

  constructor(public userService: UserService, private tokenService: TokenService, private notificationService: NotificationService,
              private dialog: MatDialog, public userRoleUtil: UserRoleUtil, private activatedRoute: ActivatedRoute, private router: Router) {
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
      switchMap(term => term.length < 3 ? of([]) : this.userService.getAllUsersFiltered(term, term).pipe(
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

  onSelect($event: any) {
    const selectedUser: User = $event.source.value
    this.router.navigate(["profile/" + selectedUser.username]).then();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(DialogContentProfileEditDialog, {
      data: {
        inputData: {
          input_isAdmin: this.userService.isAdmin(this.currentUser),
          input_nickname: this.currentUser.nickname,
        },
        outputData: {
          submitted: false,
          password: "",
          nickname: "",
          email: "",
          roles: [...this.profileUser.roles]
        }
      }
    });

    dialogRef.afterClosed().subscribe(
      result => {
        if (result.submitted) {
          const userUpdateRequest: UserUpdateRequest = new UserUpdateRequest(this.profileUser.id, null, result.password,
            result.nickname, result.email, result.roles);

          this.userService.updateUser(this.profileUser.id, userUpdateRequest).subscribe(
            (updatedUser: User) => {
              this.router.navigate(["profile/" + updatedUser.username], {queryParams: {updated: 1}}).then();
            }, (error) => {
              this.notificationService.showError(error.error);
            }
          )
        }
      }
    )
  }

  signOut() {
    this.tokenService.signout();
    this.notificationService.showSuccess("Signed out");
    this.router.navigate([""]).then();
  }

  displayUser(user: User): string {
    return user ? user.nickname + " (" + user.username + ")" : "";
  }

}

export interface DialogData {
  inputData: DialogInputData;
  outputData: DialogOutputData;
}

export interface DialogInputData {
  input_isAdmin: boolean;
  input_nickname: string;
}

export interface DialogOutputData {
  submitted: boolean;
  // username: string;
  password: string;
  nickname: string;
  email: string;
  roles: UserRole[];
}

@Component({
    selector: "dialog-content-profile-edit-dialog",
    templateUrl: "profile.edit-dialog.component.html"
  }
)
export class DialogContentProfileEditDialog implements OnInit {

  // public usernameControl = new FormControl("", [Validators.minLength(3), Validators.maxLength(15)]);
  public nicknameControl = new FormControl("", [Validators.minLength(3), Validators.maxLength(15)]);
  public passwordControl = new FormControl("", [Validators.minLength(8), Validators.maxLength(255)]);
  public confirmPasswordControl = new FormControl("", []);
  public emailControl = new FormControl("", [Validators.email]);

  private form: FormGroup;
  public matcher: ErrorStateMatcher = new MatchingErrorStateMatcher();

  public roleControl = new FormControl();
  public chipsSelectable = false;
  public chipsRemovable = true;
  public separatorKeysCodes: number[] = [ENTER];
  public availableRoles: UserRole[];

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;

  constructor(public dialogRef: MatDialogRef<DialogContentProfileEditDialog>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData, public formBuilder: FormBuilder, public userRoleUtil: UserRoleUtil) {
  }

  ngOnInit(): void {
    this.dialogRef.beforeClosed().subscribe(() => this.dialogRef.close(this.data.outputData));
    this.form = this.formBuilder.group({
      // username: this.usernameControl,
      password: this.passwordControl,
      confirmPassword: this.confirmPasswordControl,
      nickname: this.nicknameControl,
      email: this.emailControl
    }, {
      validators: [Validation.match("password", "confirmPassword")]
    })

    this.availableRoles = UserRoleUtil.getUserRoleArray();
    for (const role of this.data.outputData.roles) {
      Utility.removeFromArray(role, this.availableRoles)
    }
  }

  onCancel(): void {
    this.dialogRef.close(this.data.outputData);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }
    this.data.outputData.submitted = true;
    this.dialogRef.close(this.data.outputData)
  }

  addChip(event: MatChipInputEvent): void {
    const value = (event.value || '').trim().toUpperCase();

    const roleToAdd: UserRole = UserRole[value as keyof typeof UserRole];

    if (roleToAdd && !this.data.outputData.roles.includes(roleToAdd)) {
      this.data.outputData.roles.push(roleToAdd);
    }

    event.chipInput!.clear();
    this.roleControl.setValue(null);
  }

  removeChip(role: UserRole): void {
    Utility.removeFromArray(role, this.data.outputData.roles)

    if (!this.availableRoles.includes(role)) {
      this.availableRoles.push(role);
    }
  }

  selectAutocompleteChip(event: MatAutocompleteSelectedEvent): void {
    const roleToAdd: UserRole = event.option.value

    if (roleToAdd && !this.data.outputData.roles.includes(roleToAdd)) {
      this.data.outputData.roles.push(roleToAdd);

      Utility.removeFromArray(roleToAdd, this.availableRoles);
    }

    this.roleInput.nativeElement.value = "";
    this.roleControl.setValue(null);
  }

}
