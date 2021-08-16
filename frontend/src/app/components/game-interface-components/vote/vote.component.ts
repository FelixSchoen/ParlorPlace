import {Directive, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Vote, VoteCollection} from "../../../dto/vote";
import {Player} from "../../../dto/player";

@Directive({
  selector: 'app-vote',
})
export abstract class VoteComponent<P extends Player, V extends Vote<T, C>, T, C extends VoteCollection<T>> implements OnInit, OnChanges {

  @Input() public currentPlayer: P;
  @Input() public vote: V;

  public voteMap: Map<number, C>;

  protected constructor() { }

  ngOnInit(): void {
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    this.voteMap = Vote.toMap<C>(this.vote.voteCollectionMap);
  }

  abstract getTranslationKey(e: Object): string;

}
