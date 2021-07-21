import {Component, OnInit} from '@angular/core';
import {LobbyComponent} from "../lobby.component";
import {GameService} from "../../../services/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Game, GameIdentifier, WerewolfGame} from "../../../dto/game";
import {NotificationService} from "../../../services/notification.service";
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-werewolf-lobby',
  templateUrl: './werewolf-lobby.component.html',
  styleUrls: ['./werewolf-lobby.component.css']
})
export class WerewolfLobbyComponent extends LobbyComponent implements OnInit {

  constructor(public userService: UserService, public gameService: GameService<WerewolfGame>, public notificationService: NotificationService, public activatedRoute: ActivatedRoute, public router: Router) {
    super(userService, gameService, notificationService, activatedRoute, router)
  }

  ngOnInit(): void {
    this.initialize();
    this.refresh();
  }

  drop(event: CdkDragDrop<string[]>) {
    console.log(event.previousIndex)
    console.log(event.currentIndex)
  }

}
