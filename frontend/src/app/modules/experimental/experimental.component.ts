import {Component} from "@angular/core";
import werewolfpack from "../../../assets/audio/voicepack/werewolf/default/de/pack.json"

@Component({
  selector: 'app-experimental',
  templateUrl: './experimental.component.html',
  styleUrls: ['./experimental.component.css']
})
export class ExperimentalComponent {

  public action(): void {
    console.log(werewolfpack)
    console.log(werewolfpack.name)
    console.log(werewolfpack.codename.alfa)
  }

}
