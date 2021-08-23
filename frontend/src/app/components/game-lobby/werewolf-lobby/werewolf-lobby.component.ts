import {Component} from '@angular/core';
import {GameLobbyComponent} from "../game-lobby.component";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../services/notification.service";
import {UserService} from "../../../services/user.service";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../../../enums/games/werewolf-role-type";
import {WerewolfGameService} from "../../../services/werewolf-game.service";
import {CommunicationService} from "../../../services/communication.service";
import {WerewolfGame, WerewolfLobbyChangeRequest, WerewolfPlayer} from "../../../dto/werewolf";

@Component({
  selector: 'app-werewolf-lobby',
  templateUrl: './werewolf-lobby.component.html',
  styleUrls: ['./werewolf-lobby.component.scss']
})
export class WerewolfLobbyComponent extends GameLobbyComponent<WerewolfGame, WerewolfPlayer> {

  public werewolfRoleTypeArray: WerewolfRoleType[] = WerewolfRoleTypeUtil.getArray();

  constructor(
    public userService: UserService,
    public gameService: WerewolfGameService,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ) {
    super(userService, gameService, communicationService, notificationService, activatedRoute, router)
  }

  changeLobby(): void {
    let lobbyChangeRequest = new WerewolfLobbyChangeRequest(this.game.players, this.game.ruleSet);
    this.gameService.changeLobby(this.gameIdentifier, lobbyChangeRequest).subscribe(
      () => this.refreshGame(),
      (error => this.notificationService.showError(error.error))
    );
  }

  changeRoles(roles: WerewolfRoleType[]): void {
    this.game.ruleSet.gameRoleTypes = roles;
    this.changeLobby();
  }

}
