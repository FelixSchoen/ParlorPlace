import {User} from "./user";
import {LobbyRole} from "../enums/lobbyrole";
import {PlayerState} from "../enums/playerstate";

export abstract class Player {
  protected constructor(public id: number,
                        public user: User,
                        public lobbyRole: LobbyRole,
                        public playerState: PlayerState,

                        public position: number) {
  }
}

