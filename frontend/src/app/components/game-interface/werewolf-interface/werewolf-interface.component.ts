import {ChangeDetectionStrategy, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {GameInterfaceComponent} from "../game-interface.component";
import {
  WerewolfGame,
  WerewolfLogEntry,
  WerewolfPlayer,
  WerewolfPlayerWerewolfVote,
  WerewolfVote
} from "../../../dto/werewolf";
import {UserService} from "../../../services/user.service";
import {WerewolfGameService} from "../../../services/werewolf-game.service";
import {CommunicationService} from "../../../services/communication.service";
import {NotificationService} from "../../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../../../enums/games/werewolf-role-type";
import {LogEntryListComponent} from "../../game-common/interface-components/log-entry-list/log-entry-list.component";
import {WerewolfResourcePack} from "../../../entities/resource-pack";
import {LoadJsonService} from "../../../services/load-json.service";
import {WerewolfResourcePackType} from "../../../enums/games/werewolf-resource-pack-type";
import {LanguageIdentifier} from "../../../enums/language-identifier";
import {AudioService} from "../../../services/audio.service";
import {WerewolfLogType} from "../../../enums/games/werewolf-log-type";
import _ from "lodash";
import {WerewolfVoteIdentifier} from "../../../enums/games/werewolf-vote-identifier";
import {TabTitleService} from "../../../services/tab-title.service";

@Component({
  selector: 'app-werewolf-interface',
  templateUrl: './werewolf-interface.component.html',
  styleUrls: ['./werewolf-interface.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class WerewolfInterfaceComponent extends GameInterfaceComponent<WerewolfGame, WerewolfPlayer, WerewolfPlayerWerewolfVote, WerewolfResourcePack> {

  public static LOG_TAB = 2;
  public static INFO_TAB = 3;

  @ViewChild(LogEntryListComponent) logEntryList: LogEntryListComponent<WerewolfLogEntry, WerewolfPlayer>

  public activeTab: number = 0;

  public viewedLog: boolean = true;
  public viewedRole: boolean;

  public activeVotes: WerewolfVote<any, any>[];
  public concludedVotes: WerewolfVote<any, any>[];
  public votableVotes: WerewolfVote<any, any>[];

  private previousLog: WerewolfLogEntry[] = [];
  // Defines for which type of log a badge should be shown under the log tab
  private logsWithNewInformation: WerewolfLogType[] = [
    WerewolfLogType.SEER_SUCCESS,
    WerewolfLogType.SEER_FAILURE,
    WerewolfLogType.LOVERS_LOVE]

  public werewolfVoteIdentifier = WerewolfVoteIdentifier;

  public werewolfRoleTypeArray: WerewolfRoleType[] = WerewolfRoleTypeUtil.getArray();

  constructor(
    public userService: UserService,
    public gameService: WerewolfGameService,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public tabTitleService: TabTitleService,
    public audioService: AudioService,
    public loadJsonService: LoadJsonService,
    public activatedRoute: ActivatedRoute,
    public ref: ChangeDetectorRef,
    public router: Router
  ) {
    super(userService, gameService, communicationService, notificationService, audioService, loadJsonService, activatedRoute, ref, router)
    this.viewedRole = false;
  }

  public selectedTabChange(event: any): void {
    this.activeTab = event.index;

    if (this.activeTab == WerewolfInterfaceComponent.LOG_TAB)
      this.viewedLog = true;
    if (this.activeTab == WerewolfInterfaceComponent.INFO_TAB)
      this.viewedRole = true;
  }

  getCurrentRoleTypeCurrentPlayer(): WerewolfRoleType {
    return this.getCurrentRoleType(this.currentPlayer);
  }

  getCurrentRoleType(player: WerewolfPlayer): WerewolfRoleType {
    return player.gameRoles[this.currentPlayer.gameRoles.length - 1].werewolfRoleType;
  }

  protected getResourcePack(): WerewolfResourcePack {
    if (this.game == undefined)
      return new WerewolfResourcePack(this.loadJsonService, WerewolfResourcePackType.DEFAULT, LanguageIdentifier.DE);
    else
      return new WerewolfResourcePack(this.loadJsonService, this.game.ruleSet.resourcePack, LanguageIdentifier.DE)
  }

  // Callbacks

  protected loadedGameCallback() {
    super.loadedGameCallback();

    this.activeVotes = this.sortVotes(this.game.votes)[0];
    this.concludedVotes = this.sortVotes(this.game.votes)[1];
    this.votableVotes = this.activeVotes.filter(vote =>
      _.findIndex([...vote.voters], (a) => {
        return a.id == this.currentPlayer.id;
      }) > -1
    )

    this.tabTitleService.setNotification(this.votableVotes.length);

    let missing = this.game.log.filter(item => _.findIndex(this.previousLog, (a) => {
      return _.isEqual(a, item)
    }) < 0);

    let showNotification = (missing.some(item => this.logsWithNewInformation.includes(item.logType)) && (this.activeTab != WerewolfInterfaceComponent.LOG_TAB));

    this.viewedLog = !showNotification;

    this.previousLog = this.game.log;
  }
}
