import {Component, ComponentFactoryResolver, OnInit, ViewChild} from '@angular/core';
import {ComponentHost} from "./component-host.directive";
import {WerewolfLobbyComponent} from "../../components/game-lobby/werewolf-lobby/werewolf-lobby.component";
import {ActivatedRoute, Router} from "@angular/router";
import {GameBaseInformation, GameIdentifier} from "../../dto/game";
import {GameType} from "../../enums/game-type";
import {NotificationService} from "../../services/notification.service";
import {environment} from "../../../environments/environment";
import {GeneralGameService} from "../../services/general-game.service";
import {GameState} from "../../enums/game-state";
import {WerewolfInterfaceComponent} from "../../components/game-interface/werewolf-interface/werewolf-interface.component";

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

  @ViewChild(ComponentHost, {static: true}) gameHost!: ComponentHost;

  public loading: boolean = true;
  public errorMessage: string = "";

  private gameComponentMap = new Map<GameType, GameComponents>([
    [GameType.WEREWOLF, {mainComponent: WerewolfInterfaceComponent, lobbyComponent: WerewolfLobbyComponent}],
  ]);

  constructor(
    private generalGameService: GeneralGameService,
    private notificationService: NotificationService,
    private activatedRoute: ActivatedRoute,
    private componentFactoryResolver: ComponentFactoryResolver,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    const queryIdentifier: string = this.activatedRoute.snapshot.params["identifier"];

    if (queryIdentifier != undefined && queryIdentifier.length > 0) {
      this.loadComponent(queryIdentifier);
    }
  }

  private loadComponent(componentIdentifier: string) {
    this.loading = true;

    this.generalGameService.getActiveGameBaseInformation(new GameIdentifier(componentIdentifier)).subscribe({
      next: (gameBaseInformation: GameBaseInformation) => {
        let gameComponent = this.gameComponentMap.get(gameBaseInformation.gameType);

        if (gameComponent == undefined) {
          console.error();
          return;
        }

        let componentToLoad;

        if (gameBaseInformation.gameState == GameState.LOBBY)
          componentToLoad = gameComponent.lobbyComponent;
        else if (gameBaseInformation.gameState == GameState.ONGOING)
          componentToLoad = gameComponent.mainComponent;
        else
          console.error();

        const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentToLoad);

        const viewContainerRef = this.gameHost.viewContainerRef;
        viewContainerRef.clear();

        viewContainerRef.createComponent(componentFactory);

        this.loading = false;
      },
      error: (error) => {
        this.notificationService.showError(error.error);
        this.router.navigate([environment.general.PROFILE_URI]).then();
      }
    });
  }

}
