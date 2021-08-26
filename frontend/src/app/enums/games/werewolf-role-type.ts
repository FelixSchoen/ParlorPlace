import {Injectable} from "@angular/core";

export enum WerewolfRoleType {
  VILLAGER = "VILLAGER",
  WEREWOLF = "WEREWOLF",
  SEER = "SEER",
  WITCH = "WITCH",
  CUPID = "CUPID",
}

@Injectable({
  providedIn: 'root',
})
export class WerewolfRoleTypeUtil {

  constructor() {
  }

  public static getArray(): WerewolfRoleType[] {
    return [WerewolfRoleType.VILLAGER, WerewolfRoleType.WEREWOLF, WerewolfRoleType.SEER, WerewolfRoleType.WITCH, WerewolfRoleType.CUPID]
  }

  public static toIconRepresentation(type: WerewolfRoleType): string {
    return "duotone-" + type.toLowerCase();
  }

}
