import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {GameService} from "../../services/game.service";
import {Game, GameIdentifier} from "../../dto/game";
import {NotificationService} from "../../services/notification.service";
import {Player} from "../../dto/player";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {GlobalValues} from "../../globals/global-values.service";

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent<G extends Game, P extends Player> implements OnInit {

  public initialLoading: boolean;
  public refreshLoading: boolean;
  public errorMessage: string;

  public gameIdentifier: GameIdentifier;
  public currentPlayer: P;
  public game: G;

  constructor(public userService: UserService, public gameService: GameService<G>, public notificationService: NotificationService, public activatedRoute: ActivatedRoute, public router: Router) {
  }

  ngOnInit(): void {
    // const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    // let redirectString = GlobalValues.GAME_URI;
    //
    // this.gameService.getGameState(new GameIdentifier(queryIdentifier)).subscribe(
    //   (result: Game) => {
    //     console.debug(result);
    //
    //     redirectString += GameTypeUtil.toStringRepresentation(result.gameType).toLowerCase() + "/" + result.gameIdentifier.token
    //
    //     if (result.gameState == GameState.LOBBY)
    //       redirectString += "/lobby"
    //     else if (!(result.gameState == GameState.ONGOING)) {
    //       this.notificationService.showError("Unknown game state");
    //       return
    //     }
    //
    //     this.router.navigate([redirectString]).then();
    //   },
    //   () => this.router.navigate([GlobalValues.ENTRY_URI]).then()
    // )
  }

  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);
  }

  protected refresh(): void {
    this.refreshLoading = true;
    this.gameService.getGameState(this.gameIdentifier).subscribe(
      {
        next: (result: G) => {
          this.game = result
          this.userService.getCurrentUser().subscribe(
            {
              next: (user: User) => {
                this.currentPlayer = <P>[...this.game.players].filter(function (player) {
                  return player.user.id == user.id;
                })[0];
                this.refreshLoading = false;
              }
            }
          )
        },
        error: () => this.router.navigate([GlobalValues.PROFILE_URI]).then()
      }
    )
  }

  protected changeLobby(): void {
  }

  public quitLobby(user: User | null): void {
    this.gameService.quitGame(this.gameIdentifier, user).subscribe(
      {
        next: () => {
          this.router.navigate([GlobalValues.PROFILE_URI]).then()
        },
        error: error => this.notificationService.showError(error.error)
      }
    )
  }

  changePlayers(players: Set<P>): void {
    this.game.players = players;
    this.changeLobby();
  }

  public isLobbyAdmin(player: P) {
    return player.lobbyRole == "ROLE_ADMIN"
  }

}
