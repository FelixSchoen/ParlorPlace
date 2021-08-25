import {Injectable} from "@angular/core";

export enum WerewolfResourcePackType {
  DEFAULT = "DEFAULT",
  OLD_MAN = "OLD_MAN"
}

@Injectable({
  providedIn: 'root',
})
export class WerewolfResourcePackTypeUtil {

  constructor() {
  }

  public static getArray(): WerewolfResourcePackType[] {
    return [WerewolfResourcePackType.DEFAULT, WerewolfResourcePackType.OLD_MAN]
  }

}
