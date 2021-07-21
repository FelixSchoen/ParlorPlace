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

  public static getGameTypeArray(): GameType[] {
    return [GameType.WEREWOLF]
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
