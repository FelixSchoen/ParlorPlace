import {Component, OnInit} from '@angular/core';
import {VoteComponent} from "../vote.component";
import {WerewolfPlayer, WerewolfVote} from "../../../../dto/werewolf";

@Component({
  selector: 'app-werewolf-vote',
  templateUrl: '../vote.component.html',
  styleUrls: ['../vote.component.scss']
})
export class WerewolfVoteComponent extends VoteComponent<WerewolfPlayer, WerewolfVote, WerewolfPlayer> implements OnInit {

  constructor() {
    super();
  }

  ngOnInit(): void {
  }

  getTranslationKey(e: Object): string {
    console.log(this.vote.voteCollectionMap[0].key)
    return "werewolf.vote." + e.valueOf().toString().toLowerCase().replace("_", ".");
  }


}
