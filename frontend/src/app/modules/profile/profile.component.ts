import {Component, ElementRef, Inject, Injector, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User, UserUpdateRequest} from "../../dto/user";
import {NotificationService} from "../../services/notification.service";
import {TokenService} from "../../authentication/token.service";
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
import {GameType, GameTypeUtil} from "../../enums/gametype";
import {Game, GameIdentifier, GameStartRequest} from "../../dto/game";
import {removeFromArray} from "../../utility/utility";
import {environment} from "../../../environments/environment";
import {WerewolfGameService} from "../../services/werewolf-game.service";
import {GeneralGameService} from "../../services/general-game.service";

export const GameServiceMap = {
  WEREWOLF: WerewolfGameService
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public profileUser: User;
  public currentUser: User;

  public loading: boolean;
  public error: boolean = false;
  public errorMessage: string;

  public userSearchControl: FormControl = new FormControl();
  public filteredOptions: Observable<User[]>;

  public userRoleToString = UserRoleUtil.toStringRepresentation;

  private gameServiceMap = new Map<GameType, any>([
    [GameType.WEREWOLF, WerewolfGameService],
  ]);

  constructor(
    public userService: UserService,
    private generalGameService: GeneralGameService,
    private tokenService: TokenService,
    private notificationService: NotificationService,
    private dialog: MatDialog,
    private injector: Injector,
    private activatedRoute: ActivatedRoute,
    private router: Router,
  ) {
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

    this.userService.getCurrentUser().subscribe({
        next: (user) => {
          this.currentUser = user;
          if (queryName == undefined) {
            this.router.navigate([environment.general.PROFILE_URI + user.username]).then();
          } else {
            this.userService.getUserByUsername(queryName).subscribe({
              next: (user: User) => {
                this.profileUser = user
                this.error = false;
              },
              error: (error) => {
                this.errorMessage = error.error;
                this.error = true;
              }
            }).add(() => this.loading = false)
          }
        }
      }
    );
  }

  onSelect($event: any) {
    const selectedUser: User = $event.source.value
    this.router.navigate([environment.general.PROFILE_URI + selectedUser.username]).then();
  }

  openEditDialog(): void {
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
          roles: [...this.profileUser.userRoles]
        }
      }
    });

    dialogRef.afterClosed().subscribe({
        next: result => {
          if (result.submitted) {
            const userUpdateRequest: UserUpdateRequest = new UserUpdateRequest(this.profileUser.id, null, result.password,
              result.nickname, result.email, result.roles);

            this.userService.updateUser(this.profileUser.id, userUpdateRequest).subscribe({
                next: (updatedUser: User) => {
                  this.router.navigate(["profile/" + updatedUser.username], {queryParams: {updated: 1}}).then();
                },
                error: (error) => {
                  this.notificationService.showError(error.error);
                }
              }
            )
          }
        }
      }
    )
  }

  openEnterGameDialog(): void {
    const dialogRef = this.dialog.open(DialogContentProfileEnterGameDialog, {
      data: {
        outputData: {
          submitted: false,
          host: false,
          identifier: "",
          game: null
        }
      }
    });

    dialogRef.afterClosed().subscribe({
        next: result => {
          if (result.host) {
            let gameServiceType = this.gameServiceMap.get(result.gameType);
            if (gameServiceType == undefined) {
              console.error("Game service could not be found");
              return;
            }

            let gameService = this.injector.get(gameServiceType);

            gameService.hostGame(new GameStartRequest(result.gameType)).subscribe({
                next: (result: Game) => {
                  this.router.navigate([environment.general.GAME_URI + result.gameIdentifier.token]).then();
                },
                error: (error: { error: string; }) => this.notificationService.showError(error.error)
              }
            )
          } else if (result.submitted) {
            this.generalGameService.getBaseInformation(new GameIdentifier(result.identifier)).subscribe({
              next: (result) => {
                let gameServiceType = this.gameServiceMap.get(result.gameType);
                if (gameServiceType == undefined) {
                  console.error("Game service could not be found");
                  return;
                }
                let gameService = this.injector.get(gameServiceType);

                gameService.joinGame(new GameIdentifier(result.gameIdentifier.token)).subscribe({
                  next: (result: Game) => {
                    this.router.navigate([environment.general.GAME_URI + result.gameIdentifier.token]).then();
                  },
                  error: (error: { error: string; }) => this.notificationService.showError(error.error)
                });

              }
            })
          }
        }
      }
    )
  }

  signOut() {
    this.tokenService.signout();
    this.notificationService.showSuccess("Signed out");
    this.router.navigate([""]).then();
  }

  goHome() {
    this.router.navigate(["/profile"]).then();
  }

  displayUser(user: User): string {
    return user ? user.nickname + " (" + user.username + ")" : "";
  }

}

export interface EditDialogData {
  inputData: EditDialogInputData;
  outputData: EditDialogOutputData;
}

export interface EditDialogInputData {
  input_isAdmin: boolean;
  input_nickname: string;
}

export interface EditDialogOutputData {
  submitted: boolean;
  // username: string;
  password: string;
  nickname: string;
  email: string;
  roles: UserRole[];
}

export interface EnterGameDialogData {
  outputData: EnterGameDialogOutputData;
}

export interface EnterGameDialogOutputData {
  submitted: boolean;
  host: boolean;
  identifier: string;
  gameType: GameType | null;
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

  public userRoleToString = UserRoleUtil.toStringRepresentation;

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;

  constructor(public dialogRef: MatDialogRef<DialogContentProfileEditDialog>,
              @Inject(MAT_DIALOG_DATA) public data: EditDialogData, public formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.dialogRef.beforeClosed().subscribe({
      next: () => this.dialogRef.close(this.data.outputData)
    });
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
      removeFromArray(role, this.availableRoles)
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
    removeFromArray(role, this.data.outputData.roles)

    if (!this.availableRoles.includes(role)) {
      this.availableRoles.push(role);
    }
  }

  selectAutocompleteChip(event: MatAutocompleteSelectedEvent): void {
    const roleToAdd: UserRole = event.option.value

    if (roleToAdd && !this.data.outputData.roles.includes(roleToAdd)) {
      this.data.outputData.roles.push(roleToAdd);

      removeFromArray(roleToAdd, this.availableRoles);
    }

    this.roleInput.nativeElement.value = "";
    this.roleControl.setValue(null);
  }

}

@Component({
    selector: "dialog-content-profile-enter-game-dialog",
    templateUrl: "profile.enter-game-dialog.component.html"
  }
)
export class DialogContentProfileEnterGameDialog implements OnInit {

  public identifierControl = new FormControl("", [Validators.minLength(4)]);

  private form: FormGroup;

  public games: GameType[] = GameTypeUtil.getGameTypeArray();
  public gameTypeToString = GameTypeUtil.toStringRepresentation;

  constructor(public dialogRef: MatDialogRef<DialogContentProfileEnterGameDialog>,
              @Inject(MAT_DIALOG_DATA) public data: EnterGameDialogData, public formBuilder: FormBuilder, public gameTypeUtil: GameTypeUtil) {
  }

  ngOnInit(): void {
    this.dialogRef.beforeClosed().subscribe({
      next: () => this.dialogRef.close(this.data.outputData)
    });
    this.form = this.formBuilder.group({
      identifier: this.identifierControl
    })
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

  onHost(game: GameType) {
    this.data.outputData.host = true;
    this.data.outputData.gameType = game;
    this.dialogRef.close(this.data.outputData);
  }

}
