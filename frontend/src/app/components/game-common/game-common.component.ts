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
const WEBSOCKET_QUEUE_URI = environment.general.WEBSOCKET_QUEUE_PRIMARY_URI;

@Directive({
  selector: 'app-game-common',
})
export abstract class GameCommonComponent<G extends Game, P extends Player> implements OnInit, OnDestroy {

  public loading: boolean = true;
  public errorMessage: string = "";

  public gameIdentifier: GameIdentifier;
  public currentPlayer: P;
  public game: G;

  protected client: CompatClient;

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
    this.communicationService.disconnectSocket(this.client);
  }

  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);
  }

  protected initializeSocket(): void {
    this.gameService.getGame(this.gameIdentifier).subscribe(
      {
        next: (result: G) => {
          if (result.gameState != GameState.CONCLUDED)
            this.client = this.communicationService.connectSocket(WEBSOCKET_URI, WEBSOCKET_QUEUE_URI + this.gameIdentifier.token, this.subscribeCallback.bind(this));
        }
      });
  }

  protected refreshGame(): void {
    this.gameService.getGame(this.gameIdentifier).subscribe(
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
                this.loading = false;
              }
            }
          )
        },
        error: () => this.router.navigate([environment.general.PROFILE_URI]).then()
      }
    )
  }

  protected subscribeCallback(payload: any) {
    let notification: ClientNotification = JSON.parse(payload.body);

    if (notification.notificationType == NotificationType.STALE_GAME_INFORMATION) {
      this.refreshGame();
    } else {
      console.error("Unknown Notification Type")
    }

  }

}
