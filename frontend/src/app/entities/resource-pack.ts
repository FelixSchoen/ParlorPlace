import {LoadJsonService} from "../services/load-json.service";
import {CodeName} from "../enums/code-name";
import {WerewolfResourcePackType} from "../enums/games/werewolf-resource-pack-type";
import {LanguageIdentifier} from "../enums/language-identifier";

export abstract class ResourcePack {

  // Paths to pack file, audio files
  protected basePath: string = "../../assets/resourcepack/";
  protected gamePath: string;
  protected packPath: string;

  // Which pack to choose
  protected packName: string;
  protected defaultPackName: string;

  // JSON File
  protected packFile: Promise<any>;

  protected constructor(protected loadJsonService: LoadJsonService) {
  }

  // Code name

  public async getCodeNameVoiceLine(codeName: CodeName): Promise<VoiceLine> {
    return this.pickRandom(await this.getVoiceLineArray(undefined, "codename", codeName.valueOf().toLowerCase(), "voiceline"));
  }

  public async getCodeNameRepresentation(codeName: CodeName): Promise<string> {
    return await this.packFile.then(value => value.codename[codeName.valueOf().toLowerCase()].name)
  }

  // Utility

  public async getVoiceLineArray(codeNames: CodeName[] | undefined, ...identifiers: string[]): Promise<VoiceLine[]> {
    let voiceLineArray: VoiceLine[] = [];
    let voiceLinePath: string = "";

    let pointer: any = await this.packFile.then(value => value);
    let isCodename: boolean = identifiers[0] == "codename"

    // Add path to voice line
    if (!isCodename) {
      identifiers.forEach((entry, index) => {
        if (index != identifiers.length - 1)
          voiceLinePath += (entry + "/");
      })
    } else {
      // Special case for code names
      voiceLinePath += "voiceline/codename/"
    }


    // Derive actual entry from JSON structure
    for (let identifier of identifiers) {
      pointer = pointer[identifier];
    }

    // Add all given voice lines
    for (let entry of pointer) {
      let voiceLine: VoiceLine = VoiceLine.fromJson(this.packPath, voiceLinePath, this, entry);

      if (codeNames != undefined)
        await voiceLine.primeWithCodeNames(codeNames);

      voiceLineArray.push(voiceLine)
    }

    return voiceLineArray;
  }

  public pickRandom(voiceLines: VoiceLine[]): VoiceLine {
    return voiceLines[Math.floor(Math.random() * voiceLines.length)]
  }

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

    this.packPath = this.gamePath + this.packName + "/" + languageIdentifier.toLowerCase() + "/"
    this.packFile = loadJsonService.loadJson(this.packPath + "pack.json").toPromise();
  }

  public async getStartVoiceLine(): Promise<VoiceLine> {
    return this.pickRandom(await this.getVoiceLineArray(undefined, "voiceline", "general", "start"));
  }

  public async getSeerWakeVoiceLine(codeName: CodeName): Promise<VoiceLine> {
    return this.pickRandom(await this.getVoiceLineArray([codeName], "voiceline", "role", "seer", "wake"));
  }

}

export class VoiceLine {

  // Paths
  private packPath: string;
  private voiceLinePath: string;

  // Resource Pack to obtain voice lines of code names
  private resourcePack: ResourcePack;

  // Fields that store information about the voice line
  public files: string[] = [];
  public order: string[] = [];
  private codeNames: VoiceLine[] = [];

  // Static extension of the files, could perhaps be changed in the future
  private extension: string = ".mp3"

  constructor() {
  }

  public static fromJson(packPath: string, voiceLinePath: string, resourcePack: ResourcePack, data: any): VoiceLine {
    let voiceLine = new VoiceLine();

    voiceLine.packPath = packPath;
    voiceLine.voiceLinePath = voiceLinePath;
    voiceLine.resourcePack = resourcePack;

    // Check audio files
    if (data.path != undefined) {
      let files: string[] = [];

      // Append actual path to entries
      for (let entry of data.path) {
        files.push(voiceLinePath + entry);
      }

      voiceLine.files = files;
    }

    // Check order of files
    if (data.order != undefined) {
      voiceLine.order = data.order
    }

    return voiceLine;
  }

  public toPathArray(): string[] {
    let stringArray: string[] = [];

    for (let entry of this.getOrderedPaths()) {
      stringArray.push(this.packPath + entry + this.extension);
    }

    return stringArray;
  }

  public async primeWithCodeNames(codeNames: CodeName[]): Promise<void> {
    let codeNameArray: VoiceLine[] = [];

    for (let entry of codeNames) {
      codeNameArray.push(await this.resourcePack.getCodeNameVoiceLine(entry).then(value => value))
    }

    this.codeNames = codeNameArray;
  }

  // Utility

  private getOrderedPaths(): string[] {
    if (this.order.length == 0)
      return this.files;

    let fileArray: string[] = [];

    // Regular expressions in order to allow for users to specify the order of the voiceline
    let regexFile = new RegExp("file_([0-9]+)");
    let regexCodename = new RegExp("codename_([0-9]+)");

    for (let directive of this.order) {

      // Check if file matches
      if (regexFile.test(directive)) {
        let match = directive.match(regexFile)

        if (match) {
          let identifier = Number(match[1]) - 1
          fileArray.push(this.files[identifier])
        }

      }

      // Check if codename matches
      if (regexCodename.test(directive)) {
        let match = directive.match(regexCodename)

        if (match) {
          let identifier = Number(match[1]) - 1
          fileArray.push(this.codeNames[identifier].getOrderedPaths()[0])
        }

      }
    }

    return fileArray;
  }

}
