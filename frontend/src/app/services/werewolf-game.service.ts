import {Injectable} from '@angular/core';
import {AbstractGameService} from "./abstract-game.service";
import {WerewolfGame} from "../dto/game";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class WerewolfGameService extends AbstractGameService<WerewolfGame> {

  SPECIFIC_GAME_URI: string = "werewolf/";

  constructor(protected httpClient: HttpClient) {
    super(httpClient);
  }

}
