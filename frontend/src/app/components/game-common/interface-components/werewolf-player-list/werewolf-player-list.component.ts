import {Component, Input} from '@angular/core';
import {WerewolfGame, WerewolfPlayer} from "../../../../dto/werewolf";
import {PlayerState} from "../../../../enums/player-state";
import {Player, PlayerUtil} from "../../../../dto/player";
import {GameState} from "../../../../enums/game-state";
import {WerewolfRoleTypeUtil} from "../../../../enums/games/werewolf-role-type";

@Component({
  selector: 'app-werewolf-player-list',
  templateUrl: './werewolf-player-list.component.html',
  styleUrls: ['./werewolf-player-list.component.scss']
})
export class WerewolfPlayerListComponent {

  @Input() game: WerewolfGame;
  @Input() players: Set<WerewolfPlayer>;

  public gameState = GameState;
  public playerState = PlayerState;

  public playerUtil = Player;
  public werewolfRoleTypeUtil = WerewolfRoleTypeUtil;

  constructor() {
  }

  public sortPlayers(
    players: Set<WerewolfPlayer>
  ): WerewolfPlayer[] {
    let playersArray = [...players];

    if (this.game.gameState == GameState.CONCLUDED) {
      playersArray.sort(PlayerUtil.compareBySeatPosition);
    } else {
      playersArray.sort((a, b) => {
        if (a.playerState == PlayerState.DECEASED && b.playerState == PlayerState.ALIVE)
          return 1
        else if (a.playerState == PlayerState.ALIVE && b.playerState == PlayerState.DECEASED)
          return 0
        else
          return PlayerUtil.compareBySeatPosition(a, b);
      })
    }

    return playersArray
  }

}
