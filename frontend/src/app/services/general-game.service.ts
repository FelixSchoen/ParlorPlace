import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GameBaseInformation, GameIdentifier} from "../dto/game";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

const GAME_GENERAL_URI = environment.BASE_URI + environment.general.GAME_API + "general/";

@Injectable({
  providedIn: 'root'
})
export class GeneralGameService {

  constructor(private httpClient: HttpClient) {
  }

  public getBaseInformation(
    gameIdentifier: GameIdentifier
  ): Observable<GameBaseInformation> {
    return this.httpClient.get<GameBaseInformation>(GAME_GENERAL_URI + "base_info/" + gameIdentifier.token);
  }

}
