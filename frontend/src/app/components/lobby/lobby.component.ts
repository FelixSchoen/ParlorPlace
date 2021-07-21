import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {GameService} from "../../services/game.service";
import {Game, GameIdentifier} from "../../dto/game";
import {GameType, GameTypeUtil} from "../../enums/gametype";
import {NotificationService} from "../../services/notification.service";
import {GameState} from "../../enums/gamestate";
import {Player} from "../../dto/player";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit {

  public loading: boolean;
  public errorMessage: string;

  protected gameIdentifier: GameIdentifier;
  public currentPlayer: Player;
  public game: Game;

  constructor(public userService: UserService, public gameService: GameService<Game>, public notificationService: NotificationService, public activatedRoute: ActivatedRoute, public router: Router) {
  }

  ngOnInit(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    let redirectString = "/game/";

    this.gameService.getGameState(new GameIdentifier(queryIdentifier)).subscribe(
      (result: Game) => {
        console.debug(result);

        redirectString += GameTypeUtil.toStringRepresentation(result.gameType).toLowerCase() + "/" + result.gameIdentifier.token

        if (result.gameState == GameState.LOBBY)
          redirectString += "/lobby"
        else if (!(result.gameState == GameState.ONGOING)) {
          this.notificationService.showError("Unknown game state");
          return
        }

        this.router.navigate([redirectString]).then();
      },
      () => this.router.navigate(["/entry"]).then()
    )
  }

  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);
  }

  protected refresh(): void {
    this.loading = true;
    this.gameService.getGameState(this.gameIdentifier).subscribe(
      (result: Game) => {
        this.game = result
        this.userService.getCurrentUser().subscribe(
          (user: User) => {
            this.currentPlayer = [...this.game.players].filter(function (player) {
              return player.user.id == user.id;
            })[0];
            this.loading = false;
          }

        )
      },
      error => this.errorMessage = error.error
    )
  }

}
