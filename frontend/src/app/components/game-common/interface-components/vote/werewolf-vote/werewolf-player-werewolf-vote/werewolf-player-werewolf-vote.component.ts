import {Component, OnInit} from '@angular/core';
import {
  WerewolfGame,
  WerewolfPlayer,
  WerewolfPlayerVoteCollection,
  WerewolfPlayerWerewolfVote
} from "../../../../../../dto/werewolf";
import {Player, PlayerUtil} from "../../../../../../dto/player";
import {WerewolfGameService} from "../../../../../../services/werewolf-game.service";
import {NotificationService} from "../../../../../../services/notification.service";
import _ from "lodash";
import {VoteComponent} from "../../vote.component";

@Component({
  selector: 'app-werewolf-player-werewolf-vote',
  templateUrl: '../../vote.component.html',
  styleUrls: ['../../vote.component.scss']
})
export class WerewolfPlayerWerewolfVoteComponent extends VoteComponent<WerewolfGame, WerewolfPlayer, WerewolfPlayerWerewolfVote, WerewolfPlayer, WerewolfPlayerVoteCollection> implements OnInit {

  constructor(
    public gameService: WerewolfGameService,
    public notificationService: NotificationService
  ) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit()
  }

  public sortSelection(t: WerewolfPlayer[] | undefined): WerewolfPlayer[] {
    return PlayerUtil.sort(t);
  }

  protected sendVoteData(voteCollection: WerewolfPlayerVoteCollection): void {
    this.gameService.vote(this.gameIdentifier, this.vote.id, voteCollection).subscribe({
      error: error => this.notificationService.showError(error.error)
    });
  }

  getTranslationKey(e: Object): string {
    return "werewolf.vote." + e.valueOf().toString().toLowerCase().replace("_", ".");
  }

  subjectToStringRepresentation(s: WerewolfPlayer): string {
    return Player.toNameRepresentation(s.user, this.players)
  }

  includedInSelection(s: WerewolfPlayer): boolean {
    return _.findIndex(this.selectedOptions, (a) => {
      return _.isEqual(a, s)
    }) > -1;
  }

}
