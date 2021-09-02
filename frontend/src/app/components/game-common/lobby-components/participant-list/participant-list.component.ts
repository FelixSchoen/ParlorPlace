import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Player, PlayerUtil} from "../../../../dto/player";
import {CdkDragDrop} from "@angular/cdk/drag-drop";

@Component({
  selector: 'app-participant-list',
  templateUrl: './participant-list.component.html',
  styleUrls: ['./participant-list.component.scss']
})
export class ParticipantListComponent<P extends Player> implements OnInit {

  @Input() players: Set<P>;
  @Input() currentPlayer: P;
  @Input() mayEdit: any;

  @Output() playersChanged = new EventEmitter<Set<P>>();

  public playerUtil = Player;

  constructor() {
  }

  ngOnInit(): void {
  }

  public sortPlayers(
    players: Set<P>
  ): P[] {
    let playersArray = [...players];
    playersArray.sort(PlayerUtil.compareBySeatPosition)
    return playersArray
  }

  public changePosition(
    event: CdkDragDrop<string[]>
  ) {
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
