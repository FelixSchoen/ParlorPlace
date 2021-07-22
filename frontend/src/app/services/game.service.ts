import {Injectable} from '@angular/core';
import {GlobalValues} from "../globals/global-values.service";
import {HttpClient} from "@angular/common/http";
import {Game, GameIdentifier, GameStartRequest} from "../dto/game";
import {Observable} from "rxjs";
import {LobbyChangeRequest} from "../dto/lobby";
import {User} from "../dto/user";

const GAME_URI = GlobalValues.BASE_URI + 'game/';

@Injectable({
  providedIn: 'root'
})
export class GameService<G extends Game> {

  constructor(private httpClient: HttpClient) {
  }

  public startGame(gameStartRequest: GameStartRequest): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + "start", gameStartRequest);
  }

  public joinGame(gameIdentifier: GameIdentifier): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + "join/" + gameIdentifier.token, null);
  }

  public quitGame(gameIdentifier: GameIdentifier, user: User | null): Observable<void> {
    return this.httpClient.post<void>(GAME_URI + "quit/" + gameIdentifier.token, user);
  }

  public changeLobby(gameIdentifier: GameIdentifier, lobbyChangeRequest: LobbyChangeRequest): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + "lobby/change/" + gameIdentifier.token, lobbyChangeRequest);
  }

  public getGameState(gameIdentifier: GameIdentifier): Observable<G> {
    return this.httpClient.get<G>(GAME_URI + "state/game/" + gameIdentifier.token);
  }


}
