import {Component} from '@angular/core';
import {GameInterfaceComponent} from "../game-interface.component";
import {WerewolfGame, WerewolfPlayer} from "../../../dto/werewolf";
import {UserService} from "../../../services/user.service";
import {WerewolfGameService} from "../../../services/werewolf-game.service";
import {CommunicationService} from "../../../services/communication.service";
import {NotificationService} from "../../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {WerewolfRoleType} from "../../../enums/games/werewolfroletype";

@Component({
  selector: 'app-werewolf-interface',
  templateUrl: './werewolf-interface.component.html',
  styleUrls: ['./werewolf-interface.component.scss']
})
export class WerewolfInterfaceComponent extends GameInterfaceComponent<WerewolfGame, WerewolfPlayer> {

  public werewolfRoleType = WerewolfRoleType;

  public viewedRole : boolean;

  constructor(
    public userService: UserService,
    public gameService: WerewolfGameService,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ) {
    super(userService, gameService, communicationService, notificationService, activatedRoute, router)
    this.viewedRole = false;
  }

  public selectedTabChange(event: any): void {
    if (event.index == 3)
      this.viewedRole = true;
  }

  getCurrentRoleType(): WerewolfRoleType {
    return this.currentPlayer.gameRoles[this.currentPlayer.gameRoles.length-1].werewolfRoleType;
  }

}
