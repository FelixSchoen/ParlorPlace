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

  public toNameRepresentation(playersInGame: Set<Player>) {
    if ([...playersInGame].some(x => x.user.nickname == this.user.nickname))
      return (this.user.nickname + " ("+this.user.username+")")
    else
      return this.user.nickname
  }

}

