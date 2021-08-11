import {User} from "./user";
import {LobbyRole} from "../enums/lobbyrole";
import {PlayerState} from "../enums/playerstate";
import {GameRole} from "./gamerole";

export abstract class Player {
  protected constructor(public id: number,
                        public user: User,
                        public lobbyRole: LobbyRole,
                        public playerState: PlayerState,
                        public gameRoles: GameRole[],
                        public position: number) {
  }
}

