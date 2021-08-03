import {Player, WerewolfPlayer} from "./player";
import {RuleSet, WerewolfRuleSet} from "./ruleset";

export abstract class LobbyChangeRequest {
  protected constructor(public players: Set<Player>,
                        public ruleSet: RuleSet) {
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
