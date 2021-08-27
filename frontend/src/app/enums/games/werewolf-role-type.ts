import {Injectable} from "@angular/core";

export enum WerewolfRoleType {
  VILLAGER = "VILLAGER",
  WEREWOLF = "WEREWOLF",
  SEER = "SEER",
  WITCH = "WITCH",
  CUPID = "CUPID",
  BODYGUARD = "BODYGUARD",
  LYCANTHROPE = "LYCANTHROPE",
  BEAR_TAMER = "BEAR_TAMER",
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
      WerewolfRoleType.LYCANTHROPE,
      WerewolfRoleType.BEAR_TAMER,]
  }

  public static toInternalRepresentation(type: WerewolfRoleType | string): string {
    let parts: string[] = type.toLowerCase().split("_")
    let finalString: string = "" + parts.shift();

    for (let part of parts) {
      finalString += part.charAt(0).toUpperCase() + part.slice(1);
    }

    return finalString;
  }

  public static toIconRepresentation(type: WerewolfRoleType): string {
    return "duotone-" + this.toInternalRepresentation(type);
  }

}
