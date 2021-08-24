import {LoadJsonService} from "../services/load-json.service";

export abstract class VoicePack {

  protected constructor(protected loadJsonService: LoadJsonService) {
  }

  protected basePath: string = "../../assets/audio/voicepack/";
  protected gamePath: string;
  protected packFile: Promise<any>;

}

export class WerewolfVoicePack extends VoicePack {

  gamePath = this.basePath + "werewolf/"

  constructor(protected loadJsonService: LoadJsonService,
              packName: string,
              languageIdentifier: string) {
    super(loadJsonService);
    this.packFile = loadJsonService.loadJson(this.gamePath + packName + "/pack.json").toPromise();
  }

  public async getCodenameAlfa() {
    for (let entry of await this.packFile.then(value => value.codename.alfa))
      VoiceLine.fromJson(entry)
    return this.packFile.then(value => value.codename.alfa)
  }

}

export class VoiceLine {

  public files: string[] = [];
  public order: string[] = [];

  constructor() {
  }

  public static fromJson(data: any): VoiceLine {
    let voiceLine = new VoiceLine();

    if (data.path != undefined) {
      voiceLine.files = data.path
    }
    if (data.order != undefined) {
      voiceLine.order = data.order
    }

    return voiceLine;
  }


}
