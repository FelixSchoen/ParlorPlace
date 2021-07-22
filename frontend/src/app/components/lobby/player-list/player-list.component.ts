import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Player} from "../../../dto/player";
import {CdkDragDrop} from "@angular/cdk/drag-drop";

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent<P extends Player> implements OnInit {

  @Input() players: Set<P>;
  @Input() currentPlayer: P;
  @Input() mayEdit: any;

  @Output() playersChanged = new EventEmitter<Set<P>>();

  constructor() {
  }

  ngOnInit(): void {
  }

  public sortPlayers(players: Set<P>): P[] {
    let playersArray = [...players];
    playersArray.sort((a, b) => (a.position > b.position) ? 1 : -1)
    return playersArray
  }

  public changePosition(event: CdkDragDrop<string[]>) {
    let oldPosition = event.previousIndex;
    let newPosition = event.currentIndex;

    if (oldPosition == newPosition)
      return

    let operation = oldPosition < newPosition ? -1 : 1;
    let playerToChange = this.sortPlayers(this.players)[oldPosition]

    for (let player of this.players) {
      if (player.position > oldPosition && player.position <= newPosition || player.position < oldPosition && player.position >= newPosition) {
        player.position += operation
      }
    }

    playerToChange.position = newPosition;
    this.playersChanged.emit(this.players);
  }

}
