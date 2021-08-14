import {Directive, Input} from '@angular/core';
import {LogEntry} from "../../../dto/logentry";
import {AbstractLogService} from "../../../services/abstract-log.service";
import {Player} from "../../../dto/player";

@Directive({
  selector: 'app-log-entry-list',
})
export abstract class LogEntryListComponent<L extends LogEntry, P extends Player> {

  @Input() log: L[];
  @Input() players: Set<P>;

  protected constructor(public logService: AbstractLogService<L, P>) {
  }

}
