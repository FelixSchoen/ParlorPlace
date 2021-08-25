import {Component} from "@angular/core";
import {AudioService} from "../../services/audio.service";
import {WerewolfResourcePack} from "../../entities/resource-pack";
import {LoadJsonService} from "../../services/load-json.service";
import {CodeName} from "../../enums/code-name";
import {WerewolfResourcePackType} from "../../enums/games/werewolf-resource-pack-type";
import {LanguageIdentifier} from "../../enums/language-identifier";

@Component({
  selector: 'app-experimental',
  templateUrl: './experimental.component.html',
  styleUrls: ['./experimental.component.css']
})
export class ExperimentalComponent {

  constructor(public voicelineService: AudioService, public jsonService: LoadJsonService) {
  }

  public action(): void {
    let pack = new WerewolfResourcePack(this.jsonService, WerewolfResourcePackType.OLD_MAN, LanguageIdentifier.DE);

    pack.getCodeNameVoiceLine(CodeName.ALFA).then(value => console.log(value))
    pack.getCodeNameRepresentation(CodeName.ALFA).then(value => console.log(value))
  }

}
