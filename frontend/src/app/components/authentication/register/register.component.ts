import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorStateMatcher} from "@angular/material/core";
import Validation, {MatchingErrorStateMatcher} from "../../../validators/Validation";
import {UserLoginRequest, UserRegisterRequest} from "../../../dto/user";
import {AuthService} from "../../../authentication/auth.service";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  private username: string;
  private password: string;
  private email: string;

  constructor(private authService: AuthService, private notificationService: NotificationService, private dialog: MatDialog, private router: Router) {
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(DialogContentSignupDialog, {
      data: {
        submitted: false,
        username: this.username,
        password: this.password,
        email: this.email
      },
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(
      {
        next: result => {
          if (result.submitted) {
            this.username = result.username;
            this.password = result.password;
            this.email = result.email;

            const userSignupRequest: UserRegisterRequest = new UserRegisterRequest(this.username, this.password, this.username, this.email);

            this.authService.register(userSignupRequest).subscribe(
              {
                next: () => {
                  this.notificationService.showSuccess("Signed up");
                  this.authService.login(new UserLoginRequest(userSignupRequest.username, userSignupRequest.password)).subscribe(
                    () => {
                      this.router.navigate(["/profile"]).then();
                    },
                    (error) => {
                      this.notificationService.showError(error.error)
                    }
                  )
                },
                error: (error) => {
                  this.notificationService.showError(error.error)
                }
              }
            )
          }
        }
      })
  }

}

export interface DialogData {
  submitted: boolean;
  username: string;
  password: string;
  email: string;
}

@Component({
  selector: "dialog-content-signup-dialog",
  templateUrl: "register.dialog.component.html",
})
export class DialogContentSignupDialog implements OnInit {

  public usernameControl = new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(15)]);
  public passwordControl = new FormControl("", [Validators.required, Validators.minLength(8), Validators.maxLength(255)]);
  public confirmPasswordControl = new FormControl("", [Validators.required]);
  public emailControl = new FormControl("", [Validators.required, Validators.email]);

  private form: FormGroup;
  public matcher: ErrorStateMatcher = new MatchingErrorStateMatcher();

  constructor(public dialogRef: MatDialogRef<DialogContentSignupDialog>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData, public formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.dialogRef.beforeClosed().subscribe({next: () => this.dialogRef.close(this.data)});
    this.form = this.formBuilder.group({
      username: this.usernameControl,
      password: this.passwordControl,
      confirmPassword: this.confirmPasswordControl,
      email: this.emailControl
    }, {
      validators: [Validation.match("password", "confirmPassword")]
    })
  }

  onCancel(): void {
    this.dialogRef.close(this.data);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }
    this.data.submitted = true;
    this.dialogRef.close(this.data)
  }

}
