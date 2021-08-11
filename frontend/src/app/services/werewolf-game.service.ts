import {Injectable} from '@angular/core';
import {AbstractGameService} from "./abstract-game.service";
import {HttpClient} from "@angular/common/http";
import {WerewolfGame} from "../dto/werewolf";

@Injectable({
  providedIn: 'root'
})
export class WerewolfGameService extends AbstractGameService<WerewolfGame> {

  SPECIFIC_GAME_URI: string = "werewolf/";

  constructor(protected httpClient: HttpClient) {
    super(httpClient);
  }

}
