import {Component} from "@angular/core";
import {AudioService} from "../../services/audio.service";
import {LoadJsonService} from "../../services/load-json.service";

@Component({
  selector: 'app-experimental',
  templateUrl: './experimental.component.html',
  styleUrls: ['./experimental.component.css']
})
export class ExperimentalComponent {

  constructor(public voicelineService: AudioService, public jsonService: LoadJsonService) {
  }

  public action(): void {
    // let pack = new WerewolfResourcePack(this.jsonService, WerewolfResourcePackType.OLD_MAN, LanguageIdentifier.DE);
    //
    // let voiceLine = pack.getStartVoiceLine();
    //
    // voiceLine.then(value => this.voicelineService.queueAudio(value.toPathArray()))
  }

}
