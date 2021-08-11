import {Injectable} from "@angular/core";
import {TitleCasePipe} from "@angular/common";

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

  public static getArray(): WerewolfRoleType[] {
    return [WerewolfRoleType.VILLAGER, WerewolfRoleType.WEREWOLF, WerewolfRoleType.SEER]
  }

  public static toStringRepresentation(type: WerewolfRoleType): string {
    return new TitleCasePipe().transform(type.valueOf())
  }

  public static toIconRepresentation(type: WerewolfRoleType): string {
    return "duotone-" + type.toLowerCase();
  }

}
