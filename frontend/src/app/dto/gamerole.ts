import {WerewolfRoleType} from "../enums/games/werewolfroletype";

export abstract class GameRole {
  protected constructor(public id: number) {
  }
}

export class WerewolfRole extends GameRole {
  protected constructor(public id: number,
                        public werewolfRoleType: WerewolfRoleType) {
    super(id);
  }
}
