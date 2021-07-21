import {Injectable} from "@angular/core";

export enum WerewolfRoleType {
  VILLAGER = "VILLAGER",
  WEREWOLF = "WEREWOLF",
  SEER = "SEER"
}

@Injectable({
  providedIn: 'root',
})
export class WerewolfRoleTypeUtil {

  constructor() {
  }

  public static getWerewolfRoleTypeArray(): WerewolfRoleType[] {
    return [WerewolfRoleType.VILLAGER, WerewolfRoleType.VILLAGER, WerewolfRoleType.SEER]
  }

  public static toStringRepresentation(type: WerewolfRoleType): string {
    switch (type) {
      case WerewolfRoleType.VILLAGER:
        return "Villager";
      case WerewolfRoleType.WEREWOLF:
        return "Werewolf";
      case WerewolfRoleType.SEER:
        return "Seer";
      default:
        return "Unknown";
    }
  }

}
