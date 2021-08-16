import {Directive, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Vote, VoteCollection} from "../../../dto/vote";
import {Player} from "../../../dto/player";
import {Subscription, timer} from "rxjs";

@Directive({
  selector: 'app-vote',
})
export abstract class VoteComponent<P extends Player, V extends Vote<T, C>, T, C extends VoteCollection<T>> implements OnInit, OnDestroy, OnChanges {

  @Input() public currentPlayer: P;
  @Input() public vote: V;

  public countDown: Subscription;
  public voteMap: Map<number, C>;
  public timeRemaining: number;

  protected constructor() {

  }

  ngOnInit(): void {
    this.timeRemaining = (this.vote.endTime*1000 - new Date().getTime())/1000
    this.countDown = timer(0, 1000).subscribe(() => this.timeRemaining = Math.max(0, this.timeRemaining - 1));
  }

  ngOnDestroy(): void {
    this.countDown.unsubscribe();
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    this.voteMap = Vote.toMap<C>(this.vote.voteCollectionMap);
  }

  abstract getTranslationKey(e: Object): string;

}
