import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {GameService} from "../../services/game.service";
import {Game, GameIdentifier} from "../../dto/game";
import {GameType, GameTypeUtil} from "../../enums/gametype";
import {NotificationService} from "../../services/notification.service";
import {GameState} from "../../enums/gamestate";
import {Player, WerewolfPlayer} from "../../dto/player";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {GlobalValues} from "../../globals/global-values.service";
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {LobbyChangeRequest} from "../../dto/lobby";

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent<G extends Game, P extends Player> implements OnInit {

  public initialLoading: boolean;
  public refreshLoading: boolean;
  public errorMessage: string;

  protected gameIdentifier: GameIdentifier;
  public currentPlayer: P;
  public game: G;

  constructor(public userService: UserService, public gameService: GameService<G>, public notificationService: NotificationService, public activatedRoute: ActivatedRoute, public router: Router) {
  }

  ngOnInit(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    let redirectString = GlobalValues.GAME_URI;

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
      () => this.router.navigate([GlobalValues.ENTRY_URI]).then()
    )
  }

  protected initialize(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];
    this.gameIdentifier = new GameIdentifier(queryIdentifier);
  }

  protected refresh(): void {
    this.refreshLoading = true;
    this.gameService.getGameState(this.gameIdentifier).subscribe(
      (result: G) => {
        this.game = result
        this.userService.getCurrentUser().subscribe(
          (user: User) => {
            this.currentPlayer = <P>[...this.game.players].filter(function (player) {
              return player.user.id == user.id;
            })[0];
            this.refreshLoading = false;
          }
        )
      },
      () => this.router.navigate([GlobalValues.ENTRY_URI]).then()
    )
  }

  public sortPlayers(players: Set<Player>): Player[] {
    let playersArray = [...players];
    playersArray.sort((a, b) => (a.position > b.position) ? 1 : -1)
    return playersArray
  }

  public changePosition(event: CdkDragDrop<string[]>) {
    let oldPosition = event.previousIndex
    let newPosition = event.currentIndex

    if (oldPosition == newPosition)
      return

    let operation = oldPosition < newPosition ? -1 : 1
    let playerToChange = this.sortPlayers(this.game.players)[oldPosition]

    for (let player of this.game.players) {
      if (player.position > oldPosition && player.position <= newPosition || player.position < oldPosition && player.position >= newPosition) {
        player.position += operation
      }
    }

    playerToChange.position = newPosition
    this.changeLobby();
  }

  public changeLobby(): void {
  }

}
