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

  public static toNameRepresentation(user: User, playersInGame: Set<Player>) {
    if ([...playersInGame].some(x => (x.user.nickname == user.nickname && x.user.username != user.username)))
      return (user.nickname + " ("+user.username+")")
    else
      return user.nickname
  }

}

