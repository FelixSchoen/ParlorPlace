import {Injectable} from '@angular/core';
import * as abstractService from "./abstract-game.service";
import {AbstractGameService} from "./abstract-game.service";
import {HttpClient} from "@angular/common/http";
import {WerewolfGame, WerewolfPlayerVoteCollection} from "../dto/werewolf";
import {GameIdentifier} from "../dto/game";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WerewolfGameService extends AbstractGameService<WerewolfGame> {

  SPECIFIC_GAME_URI: string = "werewolf/";

  constructor(protected httpClient: HttpClient) {
    super(httpClient);
  }

  public vote(
    gameIdentifier: GameIdentifier,
    voteIdentifier: number,
    voteCollection: WerewolfPlayerVoteCollection
  ): Observable<WerewolfGame> {
    return this.httpClient.post<WerewolfGame>(abstractService.GAME_URI + this.SPECIFIC_GAME_URI + "vote/" + gameIdentifier.token + "/" + voteIdentifier, voteCollection);
  }

}
