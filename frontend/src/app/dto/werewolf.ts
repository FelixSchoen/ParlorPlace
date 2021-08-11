import {GameState} from "../enums/gamestate";
import {GameType} from "../enums/gametype";
import {Game, GameIdentifier} from "./game";
import {WerewolfRoleType} from "../enums/games/werewolfroletype";
import {GameRole} from "./gamerole";
import {LobbyChangeRequest} from "./lobby";
import {User} from "./user";
import {LobbyRole} from "../enums/lobbyrole";
import {PlayerState} from "../enums/playerstate";
import {Player} from "./player";
import {RuleSet} from "./ruleset";

export class WerewolfGame extends Game {
  constructor(public id: number,
              public gameState: GameState,
              public players: Set<WerewolfPlayer>,
              public ruleSet: WerewolfRuleSet,
              public round: number,
              public startedAt: string,
              public endedAt: string | null,
              public gameIdentifier: GameIdentifier) {
    super(id, GameType.WEREWOLF, gameState, players, ruleSet, round, startedAt, endedAt, gameIdentifier);
  }
}

export class WerewolfPlayer extends Player {
  constructor(public id: number,
              public user: User,
              public lobbyRole: LobbyRole,
              public playerState: PlayerState,
              public position: number,
              public werewolfRole: WerewolfRole) {
    super(id, user, lobbyRole, playerState, position);
  }
}

export class WerewolfRuleSet extends RuleSet {
  constructor(public id: number, public gameRoleTypes: WerewolfRoleType[]) {
    super(id);
  }
}

export class WerewolfRole extends GameRole {
  protected constructor(public id: number,
                        public werewolfRoleType: WerewolfRoleType) {
    super(id);
  }
}

export class WerewolfLobbyChangeRequest extends LobbyChangeRequest {
  constructor(public players: Set<WerewolfPlayer>,
              public ruleSet: WerewolfRuleSet) {
    super(players, ruleSet);
  }

  public toJSON(): WerewolfLobbyChangeRequest {
    return Object.assign({}, this, {
      $class: 'WerewolfLobbyChangeRequestDTO'
    });
  }
}
