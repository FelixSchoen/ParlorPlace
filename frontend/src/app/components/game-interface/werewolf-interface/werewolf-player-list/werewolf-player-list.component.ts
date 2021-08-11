import {Component, Input, OnInit} from '@angular/core';
import {WerewolfPlayer} from "../../../../dto/werewolf";
import {PlayerState} from "../../../../enums/playerstate";

@Component({
  selector: 'app-werewolf-player-list',
  templateUrl: './werewolf-player-list.component.html',
  styleUrls: ['./werewolf-player-list.component.scss']
})
export class WerewolfPlayerListComponent implements OnInit {

  @Input() players: Set<WerewolfPlayer>;

  public playerState = PlayerState;

  constructor() {
  }

  ngOnInit(): void {
    console.log(this.players)
  }

  public sortPlayers(
    players: Set<WerewolfPlayer>
  ): WerewolfPlayer[] {
    let playersArray = [...players];
    playersArray.sort((a, b) => {
      if (a.playerState == PlayerState.DECEASED && b.playerState == PlayerState.ALIVE)
        return 1
      else if (a.playerState == PlayerState.ALIVE && b.playerState == PlayerState.DECEASED)
        return 0
      else
        return (a.position > b.position) ? 1 : -1
    })
    return playersArray
  }

}
