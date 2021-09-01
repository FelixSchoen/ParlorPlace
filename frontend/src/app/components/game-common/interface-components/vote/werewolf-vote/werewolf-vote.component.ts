import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {VoteComponent} from "../vote.component";
import {WerewolfGame, WerewolfPlayer, WerewolfVote} from "../../../../../dto/werewolf";
import {VoteCollection} from "../../../../../dto/vote";
import {ComponentHost} from "../../../../../modules/game/component-host.directive";
import {WerewolfVoteIdentifier} from "../../../../../enums/games/werewolf-vote-identifier";

@Component({
  selector: 'app-werewolf-vote',
  templateUrl: './werewolf-vote.component.html',
  styleUrls: ['./werewolf-vote.component.scss']
})
export class WerewolfVoteComponent<V extends WerewolfVote<T, C>, T, C extends VoteCollection<T>> extends VoteComponent<WerewolfGame, WerewolfPlayer, V, T, C> implements OnInit {

  @Input() public werewolfVoteIdentifier: WerewolfVoteIdentifier;

  @ViewChild(ComponentHost, {static: true}) componentHost!: ComponentHost;

  public loading: boolean = true;
  public errorMessage: string = "";

  constructor() {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit();
    //this.loadComponent(this.vote.voteIdentifier)
  }

  // private loadComponent(voteIdentifier: WerewolfVoteIdentifier) {
  //   let voteComponentMap = new Map<WerewolfVoteIdentifier, any>([
  //     [WerewolfVoteIdentifier.PLAYER_VOTE, WerewolfPlayerWerewolfVoteComponent],
  //   ]);
  //
  //   let componentToLoad = voteComponentMap.get(voteIdentifier);
  //   this.loadComponentIntoHost(componentToLoad, this.componentHost);
  //   this.loading = false;
  // }

  getTranslationKey(e: Object): string {
    console.log("Pseudo-Abstract Method called")
    return "";
  }

  includedInSelection(s: any): boolean {
    console.log("Pseudo-Abstract Method called")
    return false;
  }

  protected sendVoteData(voteCollection: any): void {
    console.log("Pseudo-Abstract Method called")
  }

  sortSelection(t: any[] | undefined): any[] {
    console.log("Pseudo-Abstract Method called")
    return [];
  }

  subjectToStringRepresentation(s: any): string {
    console.log("Pseudo-Abstract Method called")
    return "";
  }

}
