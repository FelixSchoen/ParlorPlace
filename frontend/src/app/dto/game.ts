import {GameType} from "../enums/gametype";
import {GameState} from "../enums/gamestate";
import {Player, WerewolfPlayer} from "./player";
import {RuleSet, WerewolfRuleSet} from "./ruleset";

export abstract class Game {
  protected constructor(public id: number,
                        public gameType: GameType,
                        public gameState: GameState,
                        public players: Set<Player>,
                        public ruleSet: RuleSet,
                        public round: number,
                        public startedAt: string,
                        public endedAt: string | null,
                        public gameIdentifier: GameIdentifier) {
  }
}

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

export class GameIdentifier {
  constructor(public token: string) {
  }
}

export class GameStartRequest {
  constructor(public gameType: GameType) {
  }
}

export class GameBaseInformation {
  constructor(public gameIdentifier: GameIdentifier,
              public gameType: GameType,
              public gameState: GameState) {
  }
}
