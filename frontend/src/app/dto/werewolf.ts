import {GameState} from "../enums/game-state";
import {GameType} from "../enums/game-type";
import {Game, GameIdentifier} from "./game";
import {WerewolfRoleType, WerewolfRoleTypeUtil} from "../enums/games/werewolf-role-type";
import {GameRole} from "./game-role";
import {LobbyChangeRequest} from "./lobby";
import {User} from "./user";
import {LobbyRole} from "../enums/lobby-role";
import {PlayerState} from "../enums/player-state";
import {Player} from "./player";
import {RuleSet} from "./rule-set";
import {LogEntry} from "./log-entry";
import {VoteState} from "../enums/vote-state";
import {VoteType} from "../enums/vote-type";
import {EnumValue} from "@angular/compiler-cli/src/ngtsc/partial_evaluator";
import {Vote, VoteCollection} from "./vote";
import {WerewolfLogType} from "../enums/games/werewolf-log-type";
import {CodeName} from "../enums/code-name";
import {WerewolfFaction} from "../enums/games/werewolf-faction";
import {WerewolfResourcePackType} from "../enums/games/werewolf-resource-pack-type";
import {WerewolfVoteIdentifier} from "../enums/games/werewolf-vote-identifier";

export class WerewolfGame extends Game {
  constructor(public id: number,
              public gameState: GameState,
              public players: Set<WerewolfPlayer>,
              public ruleSet: WerewolfRuleSet,
              public round: number,
              public votes: WerewolfPlayerWerewolfVote[],
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
              public codeName: CodeName,
              public lobbyRole: LobbyRole,
              public playerState: PlayerState,
              public gameRoles: WerewolfGameRole[],
              public position: number,
              public placement: number) {
    super(id, user, codeName, lobbyRole, playerState, gameRoles, position, placement);
  }

}

export class WerewolfRuleSet extends RuleSet {
  constructor(public id: number,
              public resourcePack: WerewolfResourcePackType,
              public gameRoleTypes: WerewolfRoleType[]) {
    super(id);
  }
}

export class WerewolfVote<T, C extends VoteCollection<T>> extends Vote<WerewolfPlayer, T, C> {

  constructor(public id: number,
              public voteState: VoteState,
              public voteType: VoteType,
              public voteIdentifier: WerewolfVoteIdentifier,
              public voteDescriptor: EnumValue,
              public voters: Set<WerewolfPlayer>,
              public voteCollectionMap: [number, C][],
              public outcome: Set<T>,
              public outcomeAmount: number,
              public endTime: number) {
    super(id, voteState, voteType, voteDescriptor, voters, voteCollectionMap, outcome, outcomeAmount, endTime);
  }

}

export class WerewolfPlayerWerewolfVote extends WerewolfVote<WerewolfPlayer, WerewolfPlayerVoteCollection> {

  constructor(public id: number,
              public voteState: VoteState,
              public voteType: VoteType,
              public voteIdentifier: WerewolfVoteIdentifier,
              public voteDescriptor: EnumValue,
              public voters: Set<WerewolfPlayer>,
              public voteCollectionMap: [number, WerewolfPlayerVoteCollection][],
              public outcome: Set<WerewolfPlayer>,
              public outcomeAmount: number,
              public endTime: number) {
    super(id, voteState, voteType, voteIdentifier, voteDescriptor, voters, voteCollectionMap, outcome, outcomeAmount, endTime);
  }

}

export class WerewolfPlayerVoteCollection extends VoteCollection<WerewolfPlayer> {

  constructor(public amountVotes: number,
              public allowAbstain: boolean,
              public abstain: boolean,
              public subjects: WerewolfPlayer[],
              public selection: WerewolfPlayer[]) {
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

export abstract class WerewolfGameRole extends GameRole {

  protected constructor(public id: number,
                        public werewolfRoleType: WerewolfRoleType,
                        public werewolfFaction: WerewolfFaction) {
    super(id);
  }

  toIconRepresentation(): string {
    return WerewolfRoleTypeUtil.toIconRepresentation(this.werewolfRoleType);
  }
}

export class VillagerWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): VillagerWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "VILLAGER"
    });
  }
}

export class WerewolfWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): WerewolfWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "WEREWOLF"
    });
  }
}

export class SeerWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): SeerWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "SEER"
    });
  }
}

export class WitchWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction,
              public hasHealed: boolean,
              public hasKilled: boolean) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): WitchWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "WITCH"
    });
  }
}

export class CupidWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction,
              public hasLinked: boolean) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): CupidWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "CUPID"
    });
  }
}

export class BodyguardWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction,
              public lastProtected: WerewolfPlayer) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): BodyguardWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "BODYGUARD"
    });
  }
}

export class LycanthropeWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction,) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): LycanthropeWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "LYCANTHROPE"
    });
  }
}

export class BearTamerWerewolfGameRole extends WerewolfGameRole {
  constructor(public id: number,
              public werewolfRoleType: WerewolfRoleType,
              public werewolfFaction: WerewolfFaction,) {
    super(id, werewolfRoleType, werewolfFaction);
  }

  public toJSON(): BearTamerWerewolfGameRole {
    return Object.assign({}, this, {
      werewolfRoleType: "BEAR_TAMER"
    });
  }
}
