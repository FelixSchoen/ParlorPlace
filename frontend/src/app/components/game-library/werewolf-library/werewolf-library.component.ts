import {Component} from '@angular/core';
import {GameLibraryComponent} from "../game-library.component";
import {WerewolfGame, WerewolfPlayer} from "../../../dto/werewolf";
import {WerewolfGameService} from "../../../services/werewolf-game.service";
import {NotificationService} from "../../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DialogContentInfoDialog} from "../../../modules/general/info-dialog/dialog-content-info-dialog.component";

@Component({
  selector: 'app-werewolf-library',
  templateUrl: './werewolf-library.component.html',
  styleUrls: ['./werewolf-library.component.scss']
})
export class WerewolfLibraryComponent extends GameLibraryComponent<WerewolfGame> {

  constructor(
    public gameService: WerewolfGameService,
    public notificationService: NotificationService,
    public dialog: MatDialog,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ) {
    super(gameService, notificationService, dialog, activatedRoute, router)
  }

  protected openResultDialog(): void {
    const playerId: number = Number(this.activatedRoute.snapshot.queryParamMap.get("player"));

    if (playerId != undefined && [...this.game.players].some(player => player.id == playerId)) {
      const player: WerewolfPlayer = [...this.game.players].filter(potentialPlayer => potentialPlayer.id == playerId)[0];

      if (player.placement == 1) {
        this.dialog.open(DialogContentInfoDialog, {
          data: {
            iconSelector: "duotone-victory",
            titleKey: "general.other.victory",
            descriptionKey: "general.other.victoryDescription"
          }
        });
      } else if (player.placement >= 1) {
        this.dialog.open(DialogContentInfoDialog, {
          data: {
            iconSelector: "duotone-defeat",
            titleKey: "general.other.defeat",
            descriptionKey: "general.other.defeatDescription"
          }
        });
      }
    }
  }

}
