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

@Component({
  selector: 'app-game-interface',
  templateUrl: './game-interface.component.html',
  styleUrls: ['./game-interface.component.scss']
})
export class GameInterfaceComponent<G extends Game, P extends Player, V extends Vote<any, any>> extends GameCommonComponent<G, P> {

  public hideVotes: boolean = true;

  public voteState = VoteState;

  constructor(
    public userService: UserService,
    public gameService: AbstractGameService<G>,
    public communicationService: CommunicationService,
    public notificationService: NotificationService,
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
      return vote.voteState == VoteState.ONGOING;
    }, sorted);
  }

}
