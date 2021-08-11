import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AbstractGameService} from "../../services/abstract-game.service";
import {Game} from "../../dto/game";
import {NotificationService} from "../../services/notification.service";
import {Player} from "../../dto/player";
import {UserService} from "../../services/user.service";
import {User} from "../../dto/user";
import {environment} from "../../../environments/environment";
import {CommunicationService} from "../../services/communication.service";
import {GameCommonComponent} from "../game-common/game-common.component";


@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export abstract class LobbyComponent<G extends Game, P extends Player> extends GameCommonComponent<G, P> {

  protected constructor(
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
    this.gameService.getGame(this.gameIdentifier).subscribe(
      {
        next: (result: G) => {
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

  protected abstract changeLobby(): void;

  public quitLobby(user: User | null): void {
    this.gameService.quitGame(this.gameIdentifier, user).subscribe(
      {
        next: () => {
          this.router.navigate([environment.general.PROFILE_URI]).then()
        },
        error: error => this.notificationService.showError(error.error)
      }
    )
  }

  public changePlayers(players: Set<P>): void {
    this.game.players = players;
    this.changeLobby();
  }

  public isLobbyAdmin(player: P) {
    return player.lobbyRole == "ROLE_ADMIN"
  }

}
