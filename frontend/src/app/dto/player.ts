import {User} from "./user";
import {LobbyRole} from "../enums/lobby-role";
import {PlayerState} from "../enums/player-state";
import {GameRole} from "./game-role";
import {CodeName} from "../enums/code-name";

export abstract class Player {
  protected constructor(public id: number,
                        public user: User,
                        public codeName: CodeName,
                        public lobbyRole: LobbyRole,
                        public playerState: PlayerState,
                        public gameRoles: GameRole[],
                        public position: number,
                        public placement: number) {
  }

  public static nicknameUnique(user: User, playersInGame: Set<Player>): boolean {
    return !([...playersInGame].some(x => (x.user.nickname == user.nickname && x.user.username != user.username)))
  }

  public static toNameRepresentation(user: User, playersInGame: Set<Player>) {
    if (this.nicknameUnique(user, playersInGame))
      return user.nickname
    else
      return (user.nickname + " (" + user.username + ")")
  }

}

export class PlayerUtil {

  public static sort<P extends Player>(array: P[] | undefined): P[] {
    if (array == undefined) return [];
    return array.sort(PlayerUtil.compareBySeatPosition)
  }

  public static compareBySeatPosition(a: Player, b: Player): number {
    if (a.position < b.position) return -1;
    if (a.position == b.position) return 0;
    return 1;
  }

}

