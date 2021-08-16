import {Component} from '@angular/core';
import {LogEntryListComponent} from "../log-entry-list.component";
import {WerewolfLogEntry, WerewolfPlayer} from "../../../../dto/werewolf";
import {WerewolfLogService} from "../../../../services/werewolf-log.service";

@Component({
  selector: 'app-werewolf-log-entry-list',
  templateUrl: '../log-entry-list.component.html',
  styleUrls: ['../log-entry-list.component.scss']
})
// TODO Want to delete this class if generic approach with the abstract log component works
export class WerewolfLogEntryListComponent extends LogEntryListComponent<WerewolfLogEntry, WerewolfPlayer> {

  constructor(logService: WerewolfLogService) {
    super(logService);
  }

}
