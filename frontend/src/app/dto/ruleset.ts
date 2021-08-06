import {WerewolfRoleType} from "../enums/games/werewolfroletype";

export abstract class RuleSet {
  protected constructor(public id: number) {
  }
}

export class WerewolfRuleSet extends RuleSet {
  constructor(public id: number, public gameRoleTypes: WerewolfRoleType[]) {
    super(id);
  }
}
