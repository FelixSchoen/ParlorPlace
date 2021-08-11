import {Component, OnDestroy, OnInit} from '@angular/core';
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
import {NotificationType} from "../../enums/notificationtype";

const WEBSOCKET_URI = environment.WEBSOCKET_BASE_URI + environment.general.WEBSOCKET_GAME_URI;
const WEBSOCKET_QUEUE_URI = environment.general.WEBSOCKET_QUEUE_PRIMARY_URI;

@Component({
  selector: 'app-game-common',
  templateUrl: './game-common.component.html',
  styleUrls: ['./game-common.component.scss']
})
export abstract class GameCommonComponent<G extends Game, P extends Player>  implements OnInit, OnDestroy {

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
    this.refresh();
  }

  ngOnDestroy(): void {
    this.communicationService.disconnectSocket(this.client);
  }

  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);

    this.client = this.communicationService.connectSocket(WEBSOCKET_URI, WEBSOCKET_QUEUE_URI + this.gameIdentifier.token, this.subscribeCallback.bind(this));
  }

  protected abstract refresh(): void

  protected subscribeCallback(payload: any) {
    let notification: ClientNotification = JSON.parse(payload.body);

    if (notification.notificationType == NotificationType.STALE_GAME_INFORMATION) {
      this.refresh();
    } else {
      console.error("Unknown Notification Type")
    }

  }

}
