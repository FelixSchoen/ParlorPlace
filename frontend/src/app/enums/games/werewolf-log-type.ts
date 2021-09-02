import {Injectable} from "@angular/core";

export enum WerewolfLogType {
  START = "START",
  END = "END",
  SLEEP = "SLEEP",
  WAKE = "WAKE",
  DEATH = "DEATH",
  VILLAGERS_VOTE = "VILLAGERS_VOTE",
  WEREWOLVES_VOTE = "WEREWOLVES_VOTE",
  SEER_SUCCESS = "SEER_SUCCESS",
  SEER_FAILURE = "SEER_FAILURE",
  WITCH_HEAL = "WITCH_HEAL",
  WITCH_KILL = "WITCH_KILL",
  HUNTER_SHOOT = "HUNTER_SHOOT",
  CUPID_LINK = "CUPID_LINK",
  LOVERS_LOVE = "LOVERS_LOVE",
  BODYGUARD_PROTECT = "BODYGUARD_PROTECT",
  BEAR_TAMER_GROWL = "BEAR_TAMER_GROWL",
  BEAR_TAMER_SILENT = "BEAR_TAMER_SILENT",
}

@Injectable({
  providedIn: 'root',
})
export class WerewolfLogTypeUtil {

  public static toIconRepresentation(type: WerewolfLogType): string {
    let iconString = "";

    if (type == undefined)
      return "duotone-unknown";

    switch (type) {
      case WerewolfLogType.START:
      case WerewolfLogType.END:
        iconString = "info";
        break;
      case WerewolfLogType.SLEEP:
      case WerewolfLogType.WAKE:
        iconString = "bed";
        break;
      case WerewolfLogType.DEATH:
        iconString = "death";
        break;
      case WerewolfLogType.VILLAGERS_VOTE:
        iconString = "villager";
        break;
      case WerewolfLogType.WEREWOLVES_VOTE:
        iconString = "werewolf";
        break;
      case WerewolfLogType.SEER_SUCCESS:
      case WerewolfLogType.SEER_FAILURE:
        iconString = "seer";
        break;
      case WerewolfLogType.WITCH_HEAL:
      case WerewolfLogType.WITCH_KILL:
        iconString = "witch";
        break;
      case WerewolfLogType.HUNTER_SHOOT:
        iconString = "hunter";
        break;
      case WerewolfLogType.CUPID_LINK:
      case WerewolfLogType.LOVERS_LOVE:
        iconString = "cupid";
        break;
      case WerewolfLogType.BODYGUARD_PROTECT:
        iconString = "bodyguard";
        break;
      case WerewolfLogType.BEAR_TAMER_GROWL:
      case WerewolfLogType.BEAR_TAMER_SILENT:
        iconString = "bearTamer";
        break;
      default:
        iconString = "unknown";
    }

    return "duotone-" + iconString;
  }

}
