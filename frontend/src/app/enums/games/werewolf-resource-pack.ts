import {Injectable} from "@angular/core";

export enum WerewolfResourcePack {
  DEFAULT = "DEFAULT",
  OLD_MAN_DE = "OLD_MAN_DE"
}

@Injectable({
  providedIn: 'root',
})
export class WerewolfResourcePackUtil {

  constructor() {
  }

  public static getArray(): WerewolfResourcePack[] {
    return [WerewolfResourcePack.DEFAULT, WerewolfResourcePack.OLD_MAN_DE]
  }

}
