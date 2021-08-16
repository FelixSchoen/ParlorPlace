import {Component, OnInit} from '@angular/core';
import {VoteComponent} from "../vote.component";
import {WerewolfPlayer, WerewolfVote, WerewolfVoteCollection} from "../../../../dto/werewolf";
import {Player} from "../../../../dto/player";

@Component({
  selector: 'app-werewolf-vote',
  templateUrl: '../vote.component.html',
  styleUrls: ['../vote.component.scss']
})
export class WerewolfVoteComponent extends VoteComponent<WerewolfPlayer, WerewolfVote, WerewolfPlayer, WerewolfVoteCollection> implements OnInit {

  constructor() {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit()
  }

  getTranslationKey(e: Object): string {
    return "werewolf.vote." + e.valueOf().toString().toLowerCase().replace("_", ".");
  }

  subjectToStringRepresentation(s: WerewolfPlayer): string {
    return Player.toNameRepresentation(s.user, this.players)
  }

  includedInSelection(s: any): boolean {
    let array = this.voteMap.get(this.currentPlayer.id)?.selection
    if (array == undefined)
      return false;
    return Array.from(array).some(entry => entry == s);
  }

}
