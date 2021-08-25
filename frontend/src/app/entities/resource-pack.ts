import {LoadJsonService} from "../services/load-json.service";
import {CodeName} from "../enums/code-name";
import {WerewolfResourcePackType} from "../enums/games/werewolf-resource-pack-type";
import {LanguageIdentifier} from "../enums/language-identifier";

export abstract class ResourcePack {

  protected constructor(protected loadJsonService: LoadJsonService) {
  }

  protected basePath: string = "../../assets/resourcepack/";
  protected packName: string;
  protected gamePath: string;
  protected packFile: Promise<any>;
  protected defaultPackName: string;

  public abstract getCodeNameRepresentation(codeName: CodeName): Promise<string>;

}

export class WerewolfResourcePack extends ResourcePack {

  gamePath = this.basePath + "werewolf/"
  defaultPackName = "old_man"

  constructor(protected loadJsonService: LoadJsonService,
              packIdentifier: WerewolfResourcePackType,
              languageIdentifier: LanguageIdentifier) {
    super(loadJsonService);

    if (packIdentifier.toUpperCase() == WerewolfResourcePackType.DEFAULT.valueOf()
      || !(packIdentifier.toUpperCase() in WerewolfResourcePackType)) {
      this.packName = this.defaultPackName;
    } else
      this.packName = packIdentifier.toLowerCase()

    this.packFile = loadJsonService.loadJson(this.gamePath + this.packName + "/" + languageIdentifier.toLowerCase() + "/pack.json").toPromise();
  }

  public async getCodeNameRepresentation(codeName: CodeName): Promise<string> {
    return await this.packFile.then(value => value.codename[codeName.valueOf().toLowerCase()].name)
  }

  public async getCodeNameVoiceLine(codeName: CodeName): Promise<VoiceLine[]> {
    let voiceLineArray: VoiceLine[] = []

    for (let entry of await this.packFile.then(value => value.codename[codeName.valueOf().toLowerCase()].voiceline))
      voiceLineArray.push(VoiceLine.fromJson(entry));

    return voiceLineArray
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
