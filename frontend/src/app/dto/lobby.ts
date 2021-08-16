import {Player} from "./player";
import {RuleSet} from "./ruleset";

export abstract class LobbyChangeRequest {
  protected constructor(public players: Set<Player>,
                        public ruleSet: RuleSet) {
  }
}

