import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {GameService} from "../../services/game.service";
import {GameIdentifier} from "../../dto/game";

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit {

  constructor(public gameService: GameService, public activatedRoute: ActivatedRoute, public router: Router) { }

  ngOnInit(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];

    this.gameService.getGameState(new GameIdentifier(queryIdentifier)).subscribe(
      result => {

      },
      error => this.router.navigate(["/entry"]).then()
    )
  }

}
