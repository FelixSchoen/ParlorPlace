import {Component, ViewChild} from '@angular/core';
import {GameInterfaceComponent} from "../game-interface.component";
import {WerewolfGame, WerewolfLogEntry, WerewolfPlayer, WerewolfVote} from "../../../dto/werewolf";
import {UserService} from "../../../services/user.service";
import {WerewolfGameService} from "../../../services/werewolf-game.service";
import {CommunicationService} from "../../../services/communication.service";
import {NotificationService} from "../../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {WerewolfRoleType} from "../../../enums/games/werewolf-role-type";
import {LogEntryListComponent} from "../../game-common/game-components/log-entry-list/log-entry-list.component";
import {WerewolfResourcePack} from "../../../entities/resource-pack";
import {LoadJsonService} from "../../../services/load-json.service";
import {WerewolfResourcePackType} from "../../../enums/games/werewolf-resource-pack-type";
import {LanguageIdentifier} from "../../../enums/language-identifier";

@Component({
  selector: 'app-werewolf-interface',
  templateUrl: './werewolf-interface.component.html',
  styleUrls: ['./werewolf-interface.component.scss']
})
export class WerewolfInterfaceComponent extends GameInterfaceComponent<WerewolfGame, WerewolfPlayer, WerewolfVote, WerewolfResourcePack> {

  @ViewChild(LogEntryListComponent) logEntryList: LogEntryListComponent<WerewolfLogEntry, WerewolfPlayer>

  public viewedRole : boolean;

  constructor(
    public userService: UserService,
    public gameService: WerewolfGameService,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public loadJsonService: LoadJsonService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ) {
    super(userService, gameService, communicationService, notificationService, loadJsonService, activatedRoute, router)
    this.viewedRole = false;
  }

  public selectedTabChange(event: any): void {
    if (event.index == 3)
      this.viewedRole = true;
  }

  getCurrentRoleTypeCurrentPlayer(): WerewolfRoleType {
    return this.getCurrentRoleType(this.currentPlayer);
  }

  getCurrentRoleType(player: WerewolfPlayer): WerewolfRoleType {
    return player.gameRoles[this.currentPlayer.gameRoles.length-1].werewolfRoleType;
  }

  protected getResourcePack(): WerewolfResourcePack {
    if (this.game == undefined)
      return new WerewolfResourcePack(this.loadJsonService, WerewolfResourcePackType.DEFAULT, LanguageIdentifier.DE);
    else
      return new WerewolfResourcePack(this.loadJsonService, this.game.ruleSet.resourcePack, LanguageIdentifier.DE)
  }
  
}
