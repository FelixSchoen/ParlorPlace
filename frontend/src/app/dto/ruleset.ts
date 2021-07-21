import {WerewolfRoleType} from "../enums/games/werewolfroletype";

export abstract class RuleSet {
  protected constructor(public id: number) {
  }
}

class List<T> {
}

export class WerewolfRuleSet extends RuleSet {
  constructor(public id: number, public roles: List<WerewolfRoleType>) {
    super(id);
  }
}
