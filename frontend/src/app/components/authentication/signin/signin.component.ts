import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {NotificationService} from "../../../services/notification.service";
import {UserSigninRequest} from "../../../dto/user";
import {Router} from "@angular/router";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent {

  private username: string;
  private password: string;

  constructor(private authService: AuthService, private notificationService: NotificationService, private dialog: MatDialog, private router: Router) {
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(DialogContentSigninDialog, {
      data: {
        submitted: false,
        username: this.username,
        password: this.password
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result.submitted) {
        this.username = result.username;
        this.password = result.password;

        const userSigninRequest: UserSigninRequest = new UserSigninRequest(this.username, this.password);

        this.authService.signin(userSigninRequest).subscribe(
          () => {
            this.notificationService.showSuccess("Signed in");
            this.router.navigate(["/profile"]).then();
          },
          (error) => {
            this.notificationService.showError(error.error)
          }
        )
      }
    });
  }

}

export interface DialogData {
  submitted: boolean;
  username: string;
  password: string;
}

@Component({
  selector: "dialog-content-signin-dialog",
  templateUrl: "signin.dialog.component.html",
})
export class DialogContentSigninDialog implements OnInit {

  public usernameControl = new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(15)]);
  public passwordControl = new FormControl("", [Validators.required, Validators.minLength(8), Validators.maxLength(255)]);

  private form: FormGroup;

  constructor(public dialogRef: MatDialogRef<DialogContentSigninDialog>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData, public formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.dialogRef.beforeClosed().subscribe(() => this.dialogRef.close(this.data));
    this.form = this.formBuilder.group({username: this.usernameControl, password: this.passwordControl});
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
