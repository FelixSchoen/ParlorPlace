import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import Validation from "../../../validators/Validation";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {User, UserSigninRequest, UserSigninResponse, UserSignupRequest} from "../../../dto/user";
import {NotificationService} from "../../../services/notification.service";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  form: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder, private modalService: NgbModal, private authService: AuthService, private notificationService: NotificationService, private router: Router) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ["", [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      password: ["", [Validators.required, Validators.minLength(8), Validators.maxLength(255)]],
      confirmPassword: ["", [Validators.required]],
      email: ["", [Validators.required, Validators.email]]
    }, {
      validators: [Validation.match("password", "confirmPassword")]
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
      const userSignupRequestDTO: UserSignupRequest = new UserSignupRequest(this.f.username.value,
        this.f.password.value,
        this.f.username.value,
        this.f.email.value);

      this.authService.signup(userSignupRequestDTO).subscribe(
        () => {
          this.notificationService.showSuccess("Signed up");
          this.authService.signin(new UserSigninRequest(userSignupRequestDTO.username, userSignupRequestDTO.password)).subscribe(
            () => {
              this.modalService.dismissAll();
              this.router.navigate(["/profile"]).then();
            }
          );
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
