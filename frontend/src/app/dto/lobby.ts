import {Player} from "./player";
import {RuleSet} from "./rule-set";

export abstract class LobbyChangeRequest {
  protected constructor(public players: Set<Player>,
                        public ruleSet: RuleSet) {
  }
}

