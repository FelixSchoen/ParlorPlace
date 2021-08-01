import {Component, ComponentFactoryResolver, OnInit, ViewChild} from '@angular/core';
import {GameDirective} from "./game.directive";
import {WerewolfLobbyComponent} from "../lobby/werewolf-lobby/werewolf-lobby.component";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  @ViewChild(GameDirective, {static: true}) gameHost!: GameDirective;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
  ) {
  }

  ngOnInit(): void {
    this.loadComponent("Werewolf");
  }

  private loadComponent(componentIdentifier: String) {
    let componentType: any;

    switch (componentIdentifier) {
      case "Werewolf":
        componentType = WerewolfLobbyComponent;
        break;
      default:
        console.log("Unknown type");
        break;
    }

    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentType);

    const viewContainerRef = this.gameHost.viewContainerRef;
    viewContainerRef.clear();

    const componentRef = viewContainerRef.createComponent(componentFactory);
  }

}
