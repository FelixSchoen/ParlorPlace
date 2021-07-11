import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AuthService} from "../../../services/auth.service";
import {NotificationService} from "../../../services/notification.service";
import Validation from "../../../validators/Validation";
import {UserSigninRequest, UserSigninResponse, UserSignupRequest} from "../../../dto/user";
import {AuthRequest, AuthResponse} from "../../../dto/authentication";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  form: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder, private modalService: NgbModal, private authService: AuthService, private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ["", [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      password: ["", [Validators.required, Validators.minLength(8), Validators.maxLength(255)]]
    })
  }

  open(content: any) {
    this.modalService.open(content, {centered: true});
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.form!.invalid) {
      return;
    } else {
      const userSigninRequest: UserSigninRequest = new UserSigninRequest(this.f.username.value,
        this.f.password.value);

      this.authService.signin(userSigninRequest).subscribe(
        (userSigninResponse: UserSigninResponse) => {
          this.notificationService.showSuccess("Signed in");
          this.modalService.dismissAll();
        }, (error) => {
          this.notificationService.showError(error.error);
        }
      )
    }
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form!.controls;
  }

}
