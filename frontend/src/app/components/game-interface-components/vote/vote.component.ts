import {Directive, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Vote, VoteCollection, VoteUtil} from "../../../dto/vote";
import {Player, PlayerUtil} from "../../../dto/player";
import {Subscription, timer} from "rxjs";
import {Game, GameIdentifier} from "../../../dto/game";
import {VoteState} from "../../../enums/vote-state";

@Directive({
  selector: 'app-vote',
})
export abstract class VoteComponent<G extends Game, P extends Player, V extends Vote<T, C>, T, C extends VoteCollection<T>> implements OnInit, OnDestroy, OnChanges {

  @Input() public currentPlayer: P;
  @Input() public gameIdentifier: GameIdentifier;
  @Input() public players: Set<P>;
  @Input() public vote: V;

  public countDown: Subscription;
  public voteMap: Map<number, C>;
  public votersData: [P, C][];
  public timeRemaining: number;

  public selectedOptions: T[];

  public voteState = VoteState;
  public displayedColumns: string[] = ["player", "selection"]

  protected constructor() {

  }

  ngOnInit(): void {
    if (this.vote.voteState == VoteState.ONGOING) {
      this.timeRemaining = (this.vote.endTime * 1000 - new Date().getTime()) / 1000
      this.countDown = timer(0, 1000).subscribe(() => this.timeRemaining = Math.max(0, this.timeRemaining - 1));
    }
    this.update();
  }

  ngOnDestroy(): void {
    if (this.countDown != undefined)
      this.countDown.unsubscribe();
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    this.update();
  }

  update(): void {
    this.voteMap = VoteUtil.toMap<C>(this.vote.voteCollectionMap);
    this.votersData = this.getData();
    this.selectedOptions = Array.from(this.voteMap.get(this.currentPlayer.id)!.selection);
  }

  public selectOption(t: T) {
    let voteCollection = this.voteMap.get(this.currentPlayer.id);
    if (voteCollection == undefined) return;

    voteCollection.abstain = false;

    this.selectedOptions.push(t);
    if (this.selectedOptions.length > voteCollection.amountVotes) {
      let removedElement = this.selectedOptions.shift();
      voteCollection.selection = voteCollection.selection.filter(obj => obj != removedElement);
    }

    voteCollection.selection.push(t);

    this.sendVoteData(voteCollection);
  }

  public selectAbstain() {
    let voteCollection = this.voteMap.get(this.currentPlayer.id);
    if (voteCollection == undefined) return;

    voteCollection.abstain = true;
    voteCollection.selection = [];

    this.selectedOptions = [];

    this.sendVoteData(voteCollection)
  }

  protected abstract sendVoteData(voteCollection: C): void;

  public abstract sortSelection(t: T[]): T[];

  public getData(): [P, C][] {
    let entries = this.voteMap.entries();
    let data: [P, C][] = [];

    for (let entry of entries) {
      let key: number = entry[0];
      let collection: C = entry[1];

      let player: P = Array.from(this.players).filter(player => player.id == key)[0];

      data.push([player, collection]);
    }

    return data.sort((a, b) => PlayerUtil.compareBySeatPosition(a[0], b[0]));
  }

  public voterToStringRepresentation(p: Player): string {
    return Player.toNameRepresentation(p.user, this.players);
  }

  abstract getTranslationKey(e: Object): string;

  abstract subjectToStringRepresentation(s: any): string;

  abstract includedInSelection(s: any): boolean;

}
