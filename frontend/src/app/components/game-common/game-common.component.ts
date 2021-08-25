import {Directive, OnDestroy, OnInit} from '@angular/core';
import {environment} from "../../../environments/environment";
import {Game, GameIdentifier} from "../../dto/game";
import {CompatClient} from "@stomp/stompjs/esm6/compatibility/compat-client";
import {Player} from "../../dto/player";
import {UserService} from "../../services/user.service";
import {AbstractGameService} from "../../services/abstract-game.service";
import {CommunicationService} from "../../services/communication.service";
import {NotificationService} from "../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ClientNotification} from "../../dto/communication";
import {NotificationType} from "../../enums/notification-type";
import {User} from "../../dto/user";
import {GameState} from "../../enums/game-state";

const WEBSOCKET_URI = environment.WEBSOCKET_BASE_URI + environment.general.WEBSOCKET_GAME_URI;
const WEBSOCKET_QUEUE_PRIMARY_URI = environment.general.WEBSOCKET_QUEUE_PRIMARY_URI;
const WEBSOCKET_QUEUE_SECONDARY_URI = environment.general.WEBSOCKET_QUEUE_SECONDARY_URI;

@Directive({
  selector: 'app-game-common',
})
export abstract class GameCommonComponent<G extends Game, P extends Player> implements OnInit, OnDestroy {

  public loading: boolean = true;
  public errorMessage: string = "";

  public gameIdentifier: GameIdentifier;
  public currentPlayer: P;
  public game: G;

  protected primaryClient: CompatClient;
  protected secondaryClient: CompatClient;

  protected constructor(
    public userService: UserService,
    public gameService: AbstractGameService<G>,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
  ) {
  }

  ngOnInit(): void {
    this.initialize();
    this.initializeSocket();
    this.refreshGame();
  }

  ngOnDestroy(): void {
    this.communicationService.disconnectSocket(this.primaryClient);
    this.communicationService.disconnectSocket(this.secondaryClient);
  }

  public isLobbyAdmin(player: P) {
    return player.lobbyRole == "ROLE_ADMIN"
  }

  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);
  }

  protected initializeSocket(): void {
    this.gameService.getActiveGame(this.gameIdentifier).subscribe(
      {
        next: (result: G) => {
          if (result.gameState != GameState.CONCLUDED) {
            this.primaryClient = this.communicationService.connectSocket(WEBSOCKET_URI, WEBSOCKET_QUEUE_PRIMARY_URI + this.gameIdentifier.token, this.subscribePrimaryCallback.bind(this));
            this.secondaryClient = this.communicationService.connectSocket(WEBSOCKET_URI, WEBSOCKET_QUEUE_SECONDARY_URI + this.gameIdentifier.token, this.subscribeSecondaryCallback.bind(this));
          }
        }
      });
  }

  protected refreshGame(): void {
    this.gameService.getActiveGame(this.gameIdentifier).subscribe(
      {
        next: (result: G) => {
          if (this.game != undefined && this.game.gameState == GameState.LOBBY && result.gameState != GameState.LOBBY) {
            const currentUrl = this.router.url
            this.router.navigateByUrl("/", {skipLocationChange: true}).then(() => {
              this.router.navigate([currentUrl]).then();
            })
            return;
          }

          this.game = result
          this.userService.getCurrentUser().subscribe(
            {
              next: (user: User) => {
                this.currentPlayer = <P>[...this.game.players].filter(function (player) {
                  return player.user.id == user.id;
                })[0];
                this.loadedGameCallback();
                this.loading = false;
              }
            }
          )
        },
        error: () => this.router.navigate([environment.general.PROFILE_URI]).then()
      }
    )
  }

  // Callbacks

  protected abstract loadedGameCallback(): void;

  protected subscribePrimaryCallback(payload: any) {
    let notification: ClientNotification = JSON.parse(payload.body);

    if (notification.notificationType == NotificationType.STALE_GAME_INFORMATION) {
      this.refreshGame();
    } else if (notification.notificationType == NotificationType.GAME_ENDED_INFORMATION) {
      if (this.game != undefined) {
        this.router.navigate([environment.general.LIBRARY_URI + this.game.id], {
          queryParams: {player: this.currentPlayer.id}
        }).then();
      } else {
        console.error("Game ended but no current game information available")
      }
    } else {
      console.error("Unknown Notification Type")
    }

  }

  protected abstract subscribeSecondaryCallback(payload: any): any;

}
