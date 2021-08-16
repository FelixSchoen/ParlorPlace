import {Directive, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Vote, VoteCollection} from "../../../dto/vote";
import {Player} from "../../../dto/player";
import {Subscription, timer} from "rxjs";

@Directive({
  selector: 'app-vote',
})
export abstract class VoteComponent<P extends Player, V extends Vote<T, C>, T, C extends VoteCollection<T>> implements OnInit, OnDestroy, OnChanges {

  @Input() public currentPlayer: P;
  @Input() public players: Set<P>;
  @Input() public vote: V;

  public countDown: Subscription;
  public voteMap: Map<number, C>;
  public votersData: [P,C][];
  public timeRemaining: number;

  public selectedOptions: T[];

  public displayedColumns: string[] = ["player", "selection"]

  protected constructor() {

  }

  ngOnInit(): void {
    this.timeRemaining = (this.vote.endTime * 1000 - new Date().getTime()) / 1000
    this.countDown = timer(0, 1000).subscribe(() => this.timeRemaining = Math.max(0, this.timeRemaining - 1));
    this.votersData = this.getData();
  }

  ngOnDestroy(): void {
    this.countDown.unsubscribe();
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    this.voteMap = Vote.toMap<C>(this.vote.voteCollectionMap);
    this.votersData = this.getData();
  }

  public selectOption() {

  }

  public getData(): [P, C][] {
    let entries = this.voteMap.entries();
    let data: [P, C][] = [];

    for (let entry of entries) {
      let key: number = entry[0];
      let collection: C = entry[1];

      let player: P = Array.from(this.players).filter(player => player.id == key)[0];

      data.push([player, collection]);
    }

    return data.sort((a,b) => (a[0].position > b[0].position) ? 1 : -1);
  }

  public voterToStringRepresentation(p: Player): string {
    return Player.toNameRepresentation(p.user, this.players);
  }

  abstract getTranslationKey(e: Object): string;

  abstract subjectToStringRepresentation(s: any): string;

  abstract includedInSelection(s: any): boolean;

}
