import {Component} from '@angular/core';
import {LogEntryListComponent} from "../log-entry-list.component";
import {WerewolfLogEntry, WerewolfPlayer} from "../../../../dto/werewolf";
import {WerewolfLogService} from "../../../../services/werewolf-log.service";
import {WerewolfLogTypeUtil} from "../../../../enums/games/werewolflogtype";

@Component({
  selector: 'app-werewolf-log-entry-list',
  templateUrl: '../log-entry-list.component.html',
  styleUrls: ['../log-entry-list.component.scss']
})
export class WerewolfLogEntryListComponent extends LogEntryListComponent<WerewolfLogEntry, WerewolfPlayer> {

  constructor(logService: WerewolfLogService) {
    super(logService);
  }

  toIconRepresentation(l: WerewolfLogEntry): string {
    return WerewolfLogTypeUtil.toIconRepresentation(l.logType);
  }



}
