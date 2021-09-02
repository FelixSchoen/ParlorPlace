import {Injectable} from '@angular/core';
import {AbstractLogService} from "./abstract-log.service";
import {TranslateService} from "@ngx-translate/core";
import {WerewolfLogEntry, WerewolfPlayer} from "../dto/werewolf";
import {WerewolfLogType} from "../enums/games/werewolf-log-type";
import {Observable} from "rxjs";
import {Player} from "../dto/player";

@Injectable({
  providedIn: 'root'
})
export class WerewolfLogService extends AbstractLogService<WerewolfLogEntry, WerewolfPlayer> {

  constructor(private translateService: TranslateService) {
    super(translateService)
  }

  toStringRepresentation(l: WerewolfLogEntry, players: Set<WerewolfPlayer>): Observable<string> {
    let textValue = l.logType.valueOf().toLowerCase();

    switch (l.logType) {
      // No additional parameters
      case WerewolfLogType.START:
      case WerewolfLogType.END:
      case WerewolfLogType.SLEEP:
      case WerewolfLogType.WAKE:
      case WerewolfLogType.BEAR_TAMER_GROWL:
      case WerewolfLogType.BEAR_TAMER_SILENT:
        return this.translateService.get("werewolf.log." + textValue);
        // Targets[0]
      case WerewolfLogType.DEATH:
      case WerewolfLogType.WEREWOLVES_VOTE:
      case WerewolfLogType.SEER_SUCCESS:
      case WerewolfLogType.SEER_FAILURE:
      case WerewolfLogType.WITCH_HEAL:
      case WerewolfLogType.WITCH_KILL:
      case WerewolfLogType.VILLAGERS_VOTE:
      case WerewolfLogType.LOVERS_LOVE:
      case WerewolfLogType.BODYGUARD_PROTECT:
        return this.translateService.get("werewolf.log." + textValue, {player: Player.toNameRepresentation(l.targets[0].user, players)});
        // Targets[0] + Targets[1]
      case WerewolfLogType.CUPID_LINK:
        return this.translateService.get("werewolf.log." + textValue, {
          player1: Player.toNameRepresentation(l.targets[0].user, players),
          player2: Player.toNameRepresentation(l.targets[1].user, players)
        });
        // Sources[0] + Targets[0]
      case WerewolfLogType.HUNTER_SHOOT:
        return this.translateService.get("werewolf.log." + textValue, {
          player1: Player.toNameRepresentation(l.sources[0].user, players),
          player2: Player.toNameRepresentation(l.targets[0].user, players)
        });
    }

    throw new Error("Unknown Werewolf Log Type")
  }

}
