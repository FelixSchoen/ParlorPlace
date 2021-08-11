import {Component, Input} from '@angular/core';
import {LogEntry} from "../../../dto/logentry";
import {AbstractLogService} from "../../../services/abstract-log.service";
import {Player} from "../../../dto/player";

@Component({
  selector: 'app-log-entry-list',
  templateUrl: './log-entry-list.component.html',
  styleUrls: ['./log-entry-list.component.scss']
})
// TODO I want to make this abstract, throws error in the module, furthermore I don't want to make a werewolf-log-entry-list-component, but I don't know if angular can resolve the AbstractLogService
export class LogEntryListComponent<L extends LogEntry, P extends Player> {

  @Input() log: L[];
  @Input() players: Set<P>;

  constructor(public logService: AbstractLogService<L, P>) {
  }

}
