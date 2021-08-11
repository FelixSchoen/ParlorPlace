import {Component} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AbstractGameService} from "../../services/abstract-game.service";
import {CommunicationService} from "../../services/communication.service";
import {NotificationService} from "../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Game} from "../../dto/game";
import {Player} from "../../dto/player";
import {GameCommonComponent} from "../game-common/game-common.component";

@Component({
  selector: 'app-game-interface',
  templateUrl: './game-interface.component.html',
  styleUrls: ['./game-interface.component.scss']
})
export class GameInterfaceComponent<G extends Game, P extends Player> extends GameCommonComponent<G, P> {

  constructor(
    public userService: UserService,
    public gameService: AbstractGameService<G>,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
  ) {
    super(userService, gameService, communicationService, notificationService, activatedRoute, router);
  }

  protected refresh(): void {
    this.loading = false;
  }

}
