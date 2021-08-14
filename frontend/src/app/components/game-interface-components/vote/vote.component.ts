import {Directive, Input, OnInit} from '@angular/core';
import {Vote} from "../../../dto/vote";
import {Player} from "../../../dto/player";

@Directive({
  selector: 'app-vote',
})
export abstract class VoteComponent<P extends Player, V extends Vote<T>, T> implements OnInit {

  @Input() public currentPlayer: P;
  @Input() public vote: V;

  protected constructor() { }

  ngOnInit(): void {
  }

  abstract getTranslationKey(e: Object): string;

}
