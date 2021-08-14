import {GameState} from "../enums/gamestate";
import {GameType} from "../enums/gametype";
import {Game, GameIdentifier} from "./game";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../enums/games/werewolfroletype";
import {GameRole} from "./gamerole";
import {LobbyChangeRequest} from "./lobby";
import {User} from "./user";
import {LobbyRole} from "../enums/lobbyrole";
import {PlayerState} from "../enums/playerstate";
import {Player} from "./player";
import {RuleSet} from "./ruleset";
import {LogEntry} from "./logentry";
import {VoteState} from "../enums/votestate";
import {VoteType} from "../enums/votetype";
import {EnumValue} from "@angular/compiler-cli/src/ngtsc/partial_evaluator";
import {Vote, VoteCollection} from "./vote";
import {WerewolfLogType} from "../enums/games/werewolflogtype";

export class WerewolfGame extends Game {
  constructor(public id: number,
              public gameState: GameState,
              public players: Set<WerewolfPlayer>,
              public ruleSet: WerewolfRuleSet,
              public round: number,
              public votes: WerewolfVote[],
              public log: WerewolfLogEntry[],
              public startedAt: string,
              public endedAt: string | null,
              public gameIdentifier: GameIdentifier) {
    super(id, GameType.WEREWOLF, gameIdentifier, gameState, players, ruleSet, round, votes, log, startedAt, endedAt);
  }
}

export class WerewolfPlayer extends Player {
  constructor(public id: number,
              public user: User,
              public lobbyRole: LobbyRole,
              public playerState: PlayerState,
              public gameRoles: WerewolfRole[],
              public position: number) {
    super(id, user, lobbyRole, playerState, gameRoles, position);
  }
}

export class WerewolfRuleSet extends RuleSet {
  constructor(public id: number, public gameRoleTypes: WerewolfRoleType[]) {
    super(id);
  }
}

export class WerewolfVote extends Vote {

  constructor(public id: number,
              public voteState: VoteState,
              public voteType: VoteType,
              public voteDescriptor: EnumValue,
              public voteCollectionMap: Map<WerewolfPlayer, WerewolfVoteCollection>,
              public outcome: Set<WerewolfPlayer>,
              public outcomeAmount: number,
              public endTime: number) {
    super(id, voteState, voteType, voteDescriptor, voteCollectionMap, outcome, outcomeAmount, endTime);
  }

}

export class WerewolfVoteCollection extends VoteCollection {

  constructor(public amountVotes: number,
              public allowAbstain: boolean,
              public abstain: boolean,
              public subjects: Set<WerewolfPlayer>,
              public selection: Set<WerewolfPlayer>) {
    super(amountVotes, allowAbstain, abstain, subjects, selection);
  }

}

export class WerewolfLogEntry extends LogEntry {
  constructor(public identifier: string,
              public sources: WerewolfPlayer[],
              public targets: WerewolfPlayer[],
              public logType: WerewolfLogType) {
    super(identifier, sources, targets);
  }
}

export class WerewolfRole extends GameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType) {
    super(id);
  }

  toIconRepresentation(): string {
    return WerewolfRoleTypeUtil.toIconRepresentation(this.werewolfRoleType);
  }
}

export class WerewolfLobbyChangeRequest extends LobbyChangeRequest {
  constructor(public players: Set<WerewolfPlayer>,
              public ruleSet: WerewolfRuleSet) {
    super(players, ruleSet);
  }

  public toJSON(): WerewolfLobbyChangeRequest {
    return Object.assign({}, this, {
      $class: 'WerewolfLobbyChangeRequestDTO'
    });
  }
}
