import {Component, OnInit} from '@angular/core';
import {LobbyComponent} from "../lobby.component";
import {GameService} from "../../../services/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {WerewolfGame} from "../../../dto/game";
import {NotificationService} from "../../../services/notification.service";
import {UserService} from "../../../services/user.service";
import {WerewolfPlayer} from "../../../dto/player";
import {WerewolfLobbyChangeRequest} from "../../../dto/lobby";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../../../enums/games/werewolfroletype";

@Component({
  selector: 'app-werewolf-lobby',
  templateUrl: './werewolf-lobby.component.html',
  styleUrls: ['./werewolf-lobby.component.scss']
})
export class WerewolfLobbyComponent extends LobbyComponent<WerewolfGame, WerewolfPlayer> implements OnInit {

  public werewolfRoleTypeToString = WerewolfRoleTypeUtil.toStringRepresentation;
  public werewolfRoleTypeArray: WerewolfRoleType[] = WerewolfRoleTypeUtil.getArray();

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
      (error => this.notificationService.showError(error.error))
    );
  }

  changeRoles(roles: WerewolfRoleType[]): void {
    this.game.ruleSet.roles = roles;
    this.changeLobby();
  }

}
