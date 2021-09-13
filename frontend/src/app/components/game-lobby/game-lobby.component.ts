import {ChangeDetectorRef, Component} from '@angular/core';
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
  templateUrl: './game-lobby.component.html',
  styleUrls: ['./game-lobby.component.css']
})
export abstract class GameLobbyComponent<G extends Game, P extends Player> extends GameCommonComponent<G, P> {

  protected constructor(
    public userService: UserService,
    public gameService: AbstractGameService<G>,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public activatedRoute: ActivatedRoute,
    public ref: ChangeDetectorRef,
    public router: Router,
  ) {
    super(userService, gameService, communicationService, notificationService, activatedRoute, ref, router);
  }

  protected abstract changeLobby(): void;

  public startGame(): void {
    this.gameService.startGame(this.gameIdentifier).subscribe({
      error: () => {}
    });
  }

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

  // Callbacks

  protected loadedGameCallback() {
  }

  protected subscribeSecondaryCallback(payload: any): any {
  }

  // Utility

  goHome() {
    this.router.navigate(["/profile"]).then();
  }

}
