import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {WerewolfGame, WerewolfPlayer} from "../../../../dto/werewolf";
import {PlayerState} from "../../../../enums/player-state";
import {PlayerUtil} from "../../../../dto/player";
import {GameState} from "../../../../enums/game-state";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../../../../enums/games/werewolf-role-type";
import _ from "lodash";

@Component({
  selector: 'app-werewolf-player-list',
  templateUrl: './werewolf-player-list.component.html',
  styleUrls: ['./werewolf-player-list.component.scss']
})
export class WerewolfPlayerListComponent implements OnChanges {

  @Input() game: WerewolfGame;
  @Input() players: Set<WerewolfPlayer>;

  public alivePlayers: WerewolfPlayer[];
  public deceasedPlayers: WerewolfPlayer[];

  public gameState = GameState;
  public playerState = PlayerState;

  public werewolfPlayer = WerewolfPlayer;
  public werewolfRoleType = WerewolfRoleType;
  public werewolfRoleTypeUtil = WerewolfRoleTypeUtil;

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.alivePlayers = this.sortPlayers(this.players)[0];
    this.deceasedPlayers = this.sortPlayers(this.players)[1];
  }

  public sortPlayers(
    players: Set<WerewolfPlayer>
  ): WerewolfPlayer[][] {
    let playersArray = [...players];

    let playerPartitions = _.partition(playersArray, function (player: WerewolfPlayer) {
      return player.playerState == PlayerState.ALIVE;
    });

    playerPartitions[0].sort(PlayerUtil.compareBySeatPosition);
    playerPartitions[1].sort(PlayerUtil.compareBySeatPosition);

    return playerPartitions;

    // if (this.game.gameState == GameState.CONCLUDED) {
    //   playersArray.sort(PlayerUtil.compareBySeatPosition);
    // } else {
    //   playersArray.sort((a, b) => {
    //     if (a.playerState == PlayerState.DECEASED && b.playerState == PlayerState.ALIVE)
    //       return 1
    //     else if (a.playerState == PlayerState.ALIVE && b.playerState == PlayerState.DECEASED)
    //       return 0
    //     else
    //       return PlayerUtil.compareBySeatPosition(a, b);
    //   })
    // }
    //
    // return playersArray
  }

}
