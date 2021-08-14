import {HttpClient} from "@angular/common/http";
import {Game, GameIdentifier, GameStartRequest} from "../dto/game";
import {Observable} from "rxjs";
import {LobbyChangeRequest} from "../dto/lobby";
import {User} from "../dto/user";
import {environment} from "../../environments/environment";

const GAME_URI = environment.BASE_URI + environment.general.GAME_API;

export abstract class AbstractGameService<G extends Game> {

  protected SPECIFIC_GAME_URI: string;

  protected constructor(protected httpClient: HttpClient) {
  }

  public hostGame(
    gameStartRequest: GameStartRequest
  ): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + this.SPECIFIC_GAME_URI + "host", gameStartRequest);
  }

  public joinGame(
    gameIdentifier: GameIdentifier
  ): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + this.SPECIFIC_GAME_URI + "join/" + gameIdentifier.token, null);
  }

  public quitGame(
    gameIdentifier: GameIdentifier,
    user: User | null
  ): Observable<void> {
    return this.httpClient.post<void>(GAME_URI + this.SPECIFIC_GAME_URI + "quit/" + gameIdentifier.token, user);
  }

  public changeLobby(
    gameIdentifier: GameIdentifier,
    lobbyChangeRequest: LobbyChangeRequest
  ): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + this.SPECIFIC_GAME_URI + "lobby/change/" + gameIdentifier.token, lobbyChangeRequest);
  }

  public startGame(
    gameIdentifier: GameIdentifier
  ): Observable<G> {
    return this.httpClient.post<G>(GAME_URI + this.SPECIFIC_GAME_URI + "start/" + gameIdentifier.token, null);
  }

  public getGame(
    gameIdentifier: GameIdentifier
  ): Observable<G> {
    return this.httpClient.get<G>(GAME_URI + this.SPECIFIC_GAME_URI + gameIdentifier.token);
  }

  public getUserActiveGames(): Observable<G[]> {
    return this.httpClient.get<G[]>(GAME_URI + this.SPECIFIC_GAME_URI + "active");
  }


}
