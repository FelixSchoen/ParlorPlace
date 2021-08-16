import {Component, OnInit} from '@angular/core';
import {VoteComponent} from "../vote.component";
import {WerewolfPlayer, WerewolfVote, WerewolfVoteCollection} from "../../../../dto/werewolf";

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


}
