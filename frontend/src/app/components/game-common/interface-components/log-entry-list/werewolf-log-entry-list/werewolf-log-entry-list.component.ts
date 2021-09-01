import {Component, Input, SimpleChanges} from '@angular/core';
import {LogEntryListComponent} from "../log-entry-list.component";
import {WerewolfLogEntry, WerewolfPlayer} from "../../../../../dto/werewolf";
import {WerewolfLogService} from "../../../../../services/werewolf-log.service";
import {WerewolfLogType, WerewolfLogTypeUtil} from "../../../../../enums/games/werewolf-log-type";

@Component({
  selector: 'app-werewolf-log-entry-list',
  templateUrl: '../log-entry-list.component.html',
  styleUrls: ['../log-entry-list.component.scss']
})
export class WerewolfLogEntryListComponent extends LogEntryListComponent<WerewolfLogEntry, WerewolfPlayer> {

  @Input() hideSleep: boolean;

  constructor(logService: WerewolfLogService) {
    super(logService);
  }

  toIconRepresentation(l: WerewolfLogEntry): string {
    if (l == undefined) return "duotone-unknown";
    return WerewolfLogTypeUtil.toIconRepresentation(l.logType);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.cleanedLog = this.getLog();
  }

  getLog(): WerewolfLogEntry[] {
    let filteredArray: WerewolfLogEntry[] = [];

    for (const [index, entry] of this.log.entries()) {
      if (!this.hideSleep || (entry.logType != WerewolfLogType.SLEEP && entry.logType != WerewolfLogType.WAKE) || index == this.log.length - 1)
        filteredArray.push(entry)
    }

    return filteredArray.reverse();
  }


}
