import {Component} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AbstractGameService} from "../../services/abstract-game.service";
import {CommunicationService} from "../../services/communication.service";
import {NotificationService} from "../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Game} from "../../dto/game";
import {Player} from "../../dto/player";
import {GameCommonComponent} from "../game-common/game-common.component";
import {Vote} from "../../dto/vote";
import {VoteState} from "../../enums/vote-state";
import * as _ from "lodash/fp";
import {ResourcePack} from "../../entities/resource-pack";
import {LoadJsonService} from "../../services/load-json.service";

@Component({
  selector: 'app-game-interface',
  templateUrl: './game-interface.component.html',
  styleUrls: ['./game-interface.component.scss']
})
export abstract class GameInterfaceComponent<G extends Game, P extends Player, V extends Vote<any, any>, RP extends ResourcePack> extends GameCommonComponent<G, P> {

  public hideVotes: boolean = true;
  public hideSleep: boolean = true;

  public resourcePack: RP;
  public codeName: Promise<string>;

  protected constructor(
    public userService: UserService,
    public gameService: AbstractGameService<G>,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
    public loadJsonService: LoadJsonService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
  ) {
    super(userService, gameService, communicationService, notificationService, activatedRoute, router);
  }

  sortVotes(votes: V[]): V[][] {
    let sorted = votes.sort((a, b) => {
      if (a.voteState == VoteState.ONGOING && b.voteState == VoteState.ONGOING)
        return 0;
      if (a.voteState == VoteState.CONCLUDED && b.voteState == VoteState.ONGOING)
        return 1;
      if (a.voteState == VoteState.ONGOING && b.voteState == VoteState.ONGOING)
        return a.endTime < b.endTime ? 0 : 1;
      else
      return a.endTime > b.endTime ? 0 : 1;
    });

    return _.partition(function(vote: V){
      return vote.voteState == VoteState.ONGOING || (new Date().getTime() - vote.endTime * 1000) / 1000 <= 5;
    }, sorted);
  }

  protected abstract getResourcePack(): RP;

  // Callbacks

  protected loadedGameCallback() {
    this.resourcePack = this.getResourcePack();
    this.codeName = this.resourcePack.getCodeNameRepresentation(this.currentPlayer.codeName);
  }

  protected subscribeSecondaryCallback(payload: any): any {
  }

}
