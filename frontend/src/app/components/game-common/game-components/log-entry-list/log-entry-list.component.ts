import {Directive, Input, SimpleChanges} from '@angular/core';
import {LogEntry} from "../../../../dto/log-entry";
import {AbstractLogService} from "../../../../services/abstract-log.service";
import {Player} from "../../../../dto/player";

@Directive({
  selector: 'app-log-entry-list',
})
export abstract class LogEntryListComponent<L extends LogEntry, P extends Player> {

  @Input() log: L[];
  cleanedLog: L[];
  @Input() players: Set<P>;

  protected constructor(public logService: AbstractLogService<L, P>) {
  }

  abstract getLog(): L[];

  abstract toIconRepresentation(l: L): string;

  abstract ngOnChanges(changes: SimpleChanges): void;

}
