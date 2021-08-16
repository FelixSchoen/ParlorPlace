import {GameType} from "../enums/gametype";
import {GameState} from "../enums/gamestate";
import {Player} from "./player";
import {RuleSet} from "./ruleset";
import {LogEntry} from "./logentry";
import {Vote} from "./vote";

export abstract class Game {
  protected constructor(public id: number,
                        public gameType: GameType,
                        public gameIdentifier: GameIdentifier,
                        public gameState: GameState,
                        public players: Set<Player>,
                        public ruleSet: RuleSet,
                        public round: number,
                        public votes: Vote<any, any>[],
                        public log: LogEntry[],
                        public startedAt: string,
                        public endedAt: string | null) {
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
