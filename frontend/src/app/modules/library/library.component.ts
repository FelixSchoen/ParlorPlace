import {Component, ComponentFactoryResolver, OnInit, ViewChild} from '@angular/core';
import {ComponentHost} from "../game/component-host.directive";
import {GameType} from "../../enums/game-type";
import {GeneralGameService} from "../../services/general-game.service";
import {NotificationService} from "../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GameBaseInformation} from "../../dto/game";
import {environment} from "../../../environments/environment";
import {WerewolfLibraryComponent} from "../../components/game-library/werewolf-library/werewolf-library.component";

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.scss']
})
export class LibraryComponent implements OnInit {

  @ViewChild(ComponentHost, {static: true}) gameHost!: ComponentHost;

  public loading: boolean = true;
  public errorMessage: string = "";

  private gameComponentMap = new Map<GameType, any>([
    [GameType.WEREWOLF, WerewolfLibraryComponent],
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
    const queryIdentifier: number = this.activatedRoute.snapshot.params["identifier"];

    if (queryIdentifier != undefined) {
      this.loadComponent(queryIdentifier);
    }
  }

  private loadComponent(componentIdentifier: number) {
    this.loading = true;

    this.generalGameService.getIndividualGameBaseInformation(componentIdentifier).subscribe({
      next: (gameBaseInformation: GameBaseInformation) => {
        let gameComponent = this.gameComponentMap.get(gameBaseInformation.gameType);

        if (gameComponent == undefined) {
          console.error();
          return;
        }

        const componentFactory = this.componentFactoryResolver.resolveComponentFactory(gameComponent);

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
