import {Component, OnInit} from '@angular/core';
import {Game} from "../../dto/game";
import {AbstractGameService} from "../../services/abstract-game.service";
import {NotificationService} from "../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-game-library',
  templateUrl: './game-library.component.html',
  styleUrls: ['./game-library.component.scss']
})
export abstract class GameLibraryComponent<G extends Game> implements OnInit {

  public game: G;

  public loading: boolean = true;
  public errorMessage: string = "";

  protected constructor(
    public gameService: AbstractGameService<G>,
    public notificationService: NotificationService,
    public dialog: MatDialog,
    public activatedRoute: ActivatedRoute,
    public router: Router,
  ) {
  }

  ngOnInit(): void {
    this.initialize();
  }

  protected initialize(): void {
    const queryIdentifier: number = this.activatedRoute.snapshot.params["identifier"];

    this.gameService.getIndividualGame(queryIdentifier).subscribe({
      next: (result: G) => {
        this.game = result;
        this.loading = false;
        this.openResultDialog();
      },
      error: () => this.router.navigate([environment.general.PROFILE_URI]).then()
    })
  }

  public goHome() {
    this.router.navigate(["/profile"]).then();
  }

  protected abstract openResultDialog(): void;

}
