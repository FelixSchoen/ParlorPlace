import {Component} from "@angular/core";
import {AudioService} from "../../services/audio.service";
import {WerewolfVoicePack} from "../../entities/voice-pack";
import {LoadJsonService} from "../../services/load-json.service";
import {CodeName} from "../../enums/code-name";

@Component({
  selector: 'app-experimental',
  templateUrl: './experimental.component.html',
  styleUrls: ['./experimental.component.css']
})
export class ExperimentalComponent {

  constructor(public voicelineService: AudioService, public jsonService: LoadJsonService) {
  }

  public action(): void {
    let pack = new WerewolfVoicePack(this.jsonService, "default", "de");
    console.log(pack.getCodename(CodeName.ALFA))
  }

}
