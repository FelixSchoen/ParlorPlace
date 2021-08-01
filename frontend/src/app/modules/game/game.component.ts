import {Component, ComponentFactoryResolver, OnInit, ViewChild} from '@angular/core';
import {GameDirective} from "./game.directive";
import {WerewolfLobbyComponent} from "../lobby/werewolf-lobby/werewolf-lobby.component";
import {ActivatedRoute, Router} from "@angular/router";
import {GameService} from "../../services/game.service";
import {Game, GameIdentifier} from "../../dto/game";
import {GameType} from "../../enums/gametype";
import {NotificationService} from "../../services/notification.service";
import {environment} from "../../../environments/environment";

export interface GameComponents {
  mainComponent?: any,
  lobbyComponent?: any,
}

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  @ViewChild(GameDirective, {static: true}) gameHost!: GameDirective;

  public loading: boolean = true;
  public errorMessage: string = "";

  private gameComponentMap = new Map<GameType, GameComponents>([
    [GameType.WEREWOLF, {lobbyComponent: WerewolfLobbyComponent}],
  ]); //TODO Add main component

  constructor(
    private gameService: GameService<Game>,
    private notificationService: NotificationService,
    private activatedRoute: ActivatedRoute,
    private componentFactoryResolver: ComponentFactoryResolver,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];

    // if (queryIdentifier != undefined && queryIdentifier.length > 0) {
    //   this.loadComponent(queryIdentifier);
    //   console.log(queryIdentifier)
    // }

    console.log(queryIdentifier)


  }

  private loadComponent(componentIdentifier: string) {
    this.loading = true;

    this.gameService.getGameState(new GameIdentifier(componentIdentifier)).subscribe({
      next: (game: Game) => {
        let gameComponent = this.gameComponentMap.get(game.gameType);

        if (gameComponent == undefined) {
          console.error("Unknown game type");
          return;
        }

        let componentToLoad;

        if (false) //TODO Check if in lobby or game state
          componentToLoad = gameComponent?.mainComponent;
        else if (true)
          componentToLoad = gameComponent.lobbyComponent;

        const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentToLoad);

        const viewContainerRef = this.gameHost.viewContainerRef;
        viewContainerRef.clear();

        const componentRef = viewContainerRef.createComponent(componentFactory);

        this.loading = false;
      },
      error: (error) => {
        this.notificationService.showError(error.error);
        this.router.navigate([environment.general.PROFILE_URI]).then();
      }
    });
  }

}
