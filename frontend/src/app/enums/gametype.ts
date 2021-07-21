import {Injectable} from "@angular/core";
import {UserRole} from "./userrole";

export enum GameType {
  WEREWOLF = "WEREWOLF"
}

@Injectable({
  providedIn: 'root',
})
export class GameTypeUtil {

  constructor() {
  }

  public static getUserRoleArray(): GameType[] {
    return [GameType.WEREWOLF]
  }

  public toStringRepresentation(type: GameType): string {
    return GameTypeUtil.toStringRepresentation(type);
  }

  public static toStringRepresentation(type: GameType): string {
    switch (type) {
      case GameType.WEREWOLF:
        return "Werewolf";
      default:
        return "Unknown";
    }
  }

}
