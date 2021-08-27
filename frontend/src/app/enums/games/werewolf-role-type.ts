import {Injectable} from "@angular/core";

export enum WerewolfRoleType {
  VILLAGER = "VILLAGER",
  WEREWOLF = "WEREWOLF",
  SEER = "SEER",
  WITCH = "WITCH",
  CUPID = "CUPID",
  BODYGUARD = "BODYGUARD",
  LYCANTHROPE = "LYCANTHROPE"
}

@Injectable({
  providedIn: 'root',
})
export class WerewolfRoleTypeUtil {

  constructor() {
  }

  public static getArray(): WerewolfRoleType[] {
    return [WerewolfRoleType.VILLAGER,
      WerewolfRoleType.WEREWOLF,
      WerewolfRoleType.SEER,
      WerewolfRoleType.WITCH,
      WerewolfRoleType.CUPID,
      WerewolfRoleType.BODYGUARD,
      WerewolfRoleType.LYCANTHROPE,]
  }

  public static toIconRepresentation(type: WerewolfRoleType): string {
    return "duotone-" + type.toLowerCase();
  }

}
