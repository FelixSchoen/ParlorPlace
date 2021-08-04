import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AbstractGameService} from "../../services/abstract-game.service";
import {Game, GameIdentifier} from "../../dto/game";
import {NotificationService} from "../../services/notification.service";
import {Player} from "../../dto/player";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {environment} from "../../../environments/environment";
import {CommunicationService} from "../../services/communication.service";
import {CompatClient} from "@stomp/stompjs/esm6/compatibility/compat-client";
import {ClientNotification} from "../../dto/communication";
import {NotificationType} from "../../enums/notificationtype";

const WEBSOCKET_URI = environment.WEBSOCKET_BASE_URI + environment.general.WEBSOCKET_GAME_URI;
const WEBSOCKET_QUEUE_URI = environment.general.WEBSOCKET_QUEUE_PRIMARY_URI;

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent<G extends Game, P extends Player> implements OnDestroy {

  public loading: boolean = true;
  public errorMessage: string;

  public gameIdentifier: GameIdentifier;
  public currentPlayer: P;
  public game: G;

  private client: CompatClient;

  constructor(
    public userService: UserService,
    public gameService: AbstractGameService<G>,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
  ) {
  }

  ngOnDestroy(): void {
    this.communicationService.disconnectSocket(this.client);
  }


  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);

    this.client = this.communicationService.connectSocket(WEBSOCKET_URI, WEBSOCKET_QUEUE_URI + this.gameIdentifier.token, this.subscribeCallback.bind(this));
  }

  protected refresh(): void {
    this.gameService.getGame(this.gameIdentifier).subscribe(
      {
        next: (result: G) => {
          this.game = result
          this.userService.getCurrentUser().subscribe(
            {
              next: (user: User) => {
                this.currentPlayer = <P>[...this.game.players].filter(function (player) {
                  return player.user.id == user.id;
                })[0];
                this.loading = false;
              }
            }
          )
        },
        error: () => this.router.navigate([environment.general.PROFILE_URI]).then()
      }
    )
  }

  protected changeLobby(): void {
  }

  public quitLobby(user: User | null): void {
    this.gameService.quitGame(this.gameIdentifier, user).subscribe(
      {
        next: () => {
          this.router.navigate([environment.general.PROFILE_URI]).then()
        },
        error: error => this.notificationService.showError(error.error)
      }
    )
  }

  public changePlayers(players: Set<P>): void {
    this.game.players = players;
    this.changeLobby();
  }

  public isLobbyAdmin(player: P) {
    return player.lobbyRole == "ROLE_ADMIN"
  }

  private subscribeCallback(payload: any) {
    let notification: ClientNotification = JSON.parse(payload.body).ClientNotification;

    if (notification.notificationType == NotificationType.STALE_GAME_INFORMATION) {
      this.refresh();
    }

  }

}
