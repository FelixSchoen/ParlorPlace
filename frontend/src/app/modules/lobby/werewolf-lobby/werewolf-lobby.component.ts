import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {LobbyComponent} from "../lobby.component";
import {GameService} from "../../../services/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {WerewolfGame} from "../../../dto/game";
import {NotificationService} from "../../../services/notification.service";
import {UserService} from "../../../services/user.service";
import {WerewolfPlayer} from "../../../dto/player";
import {WerewolfLobbyChangeRequest} from "../../../dto/lobby";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../../../enums/games/werewolfroletype";
import {FormControl} from "@angular/forms";
import {MatChipInputEvent} from "@angular/material/chips";
import {UserRole} from "../../../enums/userrole";
import {Utility} from "../../../utility/utility";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {ENTER} from "@angular/cdk/keycodes";

@Component({
  selector: 'app-werewolf-lobby',
  templateUrl: './werewolf-lobby.component.html',
  styleUrls: ['./werewolf-lobby.component.scss']
})
export class WerewolfLobbyComponent extends LobbyComponent<WerewolfGame, WerewolfPlayer> implements OnInit {

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;
  public roleControl = new FormControl();
  public separatorKeysCodes: number[] = [ENTER];

  public werewolfRoleTypeToString = WerewolfRoleTypeUtil.toStringRepresentation;
  public werewolfRoleTypeArray: WerewolfRoleType[] = WerewolfRoleTypeUtil.getWerewolfRoleTypeArray();

  constructor(public userService: UserService, public gameService: GameService<WerewolfGame>, public notificationService: NotificationService, public activatedRoute: ActivatedRoute, public router: Router) {
    super(userService, gameService, notificationService, activatedRoute, router)
  }

  ngOnInit(): void {
    this.initialize();
    this.refresh();
  }

  changeLobby(): void {
    let lobbyChangeRequest = new WerewolfLobbyChangeRequest(this.game.players, this.game.ruleSet);
    this.gameService.changeLobby(this.gameIdentifier, lobbyChangeRequest).subscribe(
      () => this.refresh(),
      (error => this.notificationService.showError(error.error()))
    );
  }

  inputChip(event: MatChipInputEvent): void {
    const value = (event.value || '').trim().toUpperCase();

    const roleToAdd: WerewolfRoleType = WerewolfRoleType[value as keyof typeof WerewolfRoleType];

    if (roleToAdd)
      this.game.ruleSet.roles.push(roleToAdd);

    event.chipInput!.clear();
    this.roleControl.setValue(null);
    this.changeLobby();
  }

  removeRole(role: WerewolfRoleType): void {
    Utility.removeFromArray(role, this.game.ruleSet.roles)
  }

  selectAutocompleteChip(event: MatAutocompleteSelectedEvent): void {
    const roleToAdd: WerewolfRoleType = event.option.value

    if (roleToAdd)
      this.game.ruleSet.roles.push(roleToAdd);

    this.roleInput.nativeElement.value = "";
    this.roleControl.setValue(null);
    this.changeLobby();
  }

}
