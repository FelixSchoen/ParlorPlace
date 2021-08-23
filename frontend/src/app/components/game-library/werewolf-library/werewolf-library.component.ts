import { Component, OnInit } from '@angular/core';
import {GameLibraryComponent} from "../game-library.component";
import {WerewolfGame} from "../../../dto/werewolf";
import {UserService} from "../../../services/user.service";
import {WerewolfGameService} from "../../../services/werewolf-game.service";
import {CommunicationService} from "../../../services/communication.service";
import {NotificationService} from "../../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-werewolf-library',
  templateUrl: './werewolf-library.component.html',
  styleUrls: ['./werewolf-library.component.scss']
})
export class WerewolfLibraryComponent extends GameLibraryComponent<WerewolfGame> {

  constructor(
    public gameService: WerewolfGameService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ) {
    super(gameService, notificationService, activatedRoute, router)
  }

}
