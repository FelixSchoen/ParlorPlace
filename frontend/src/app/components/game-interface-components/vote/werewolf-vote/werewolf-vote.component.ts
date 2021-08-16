import {Component, OnInit} from '@angular/core';
import {VoteComponent} from "../vote.component";
import {WerewolfGame, WerewolfPlayer, WerewolfVote, WerewolfVoteCollection} from "../../../../dto/werewolf";
import {Player} from "../../../../dto/player";
import {WerewolfGameService} from "../../../../services/werewolf-game.service";
import {NotificationService} from "../../../../services/notification.service";

@Component({
  selector: 'app-werewolf-vote',
  templateUrl: '../vote.component.html',
  styleUrls: ['../vote.component.scss']
})
export class WerewolfVoteComponent extends VoteComponent<WerewolfGame, WerewolfPlayer, WerewolfVote, WerewolfPlayer, WerewolfVoteCollection> implements OnInit {

  constructor(public gameService: WerewolfGameService, public notificationService: NotificationService) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit()
  }

  protected sendVoteData(voteCollection: WerewolfVoteCollection): void {
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
    let array = this.voteMap.get(this.currentPlayer.id)?.selection
    if (array == undefined)
      return false;
    return array.some(entry => entry.id == s.id);
  }

}
