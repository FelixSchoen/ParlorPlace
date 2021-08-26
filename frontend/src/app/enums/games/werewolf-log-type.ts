import {Injectable} from "@angular/core";

export enum WerewolfLogType {
  START = "START",
  END = "END",
  SLEEP = "SLEEP",
  WAKE = "WAKE",
  DEATH = "DEATH",
  WEREWOLVES_VOTE = "WEREWOLVES_VOTE",
  SEER_SUCCESS = "SEER_SUCCESS",
  SEER_FAILURE = "SEER_FAILURE",
  WITCH_HEAL = "WITCH_HEAL",
  WITCH_KILL = "WITCH_KILL",
  VILLAGERS_VOTE = "VILLAGERS_VOTE"
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
      case WerewolfLogType.VILLAGERS_VOTE:
        iconString = "villager";
        break;
      default:
        iconString = "unknown";
    }

    return "duotone-" + iconString;
  }

}