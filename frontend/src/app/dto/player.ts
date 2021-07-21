import {User} from "./user";
import {LobbyRole} from "../enums/lobbyrole";
import {PlayerState} from "../enums/playerstate";
import {WerewolfRole} from "./gamerole";

export abstract class Player {
  protected constructor(public id: number,
                        public user: User,
                        public lobbyRole: LobbyRole,
                        public playerState: PlayerState,
                        public position: number) {
  }
}

export class WerewolfPlayer extends Player {
  constructor(public id: number,
              public user: User,
              public lobbyRole: LobbyRole,
              public playerState: PlayerState,
              public position: number,
              public werewolfRole: WerewolfRole) {
    super(id, user, lobbyRole, playerState, position);
  }
}
