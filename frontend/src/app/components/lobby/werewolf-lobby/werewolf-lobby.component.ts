import {Component, OnInit} from '@angular/core';
import {LobbyComponent} from "../lobby.component";
import {GameService} from "../../../services/game.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-werewolf-lobby',
  templateUrl: './werewolf-lobby.component.html',
  styleUrls: ['./werewolf-lobby.component.css']
})
export class WerewolfLobbyComponent extends LobbyComponent implements OnInit {

  constructor(public gameService: GameService, public activatedRoute: ActivatedRoute, public router: Router) {
    super(gameService, activatedRoute, router)
  }

  ngOnInit(): void {
  }

}
