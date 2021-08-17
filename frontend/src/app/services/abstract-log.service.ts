import {Injectable} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LogEntry} from "../dto/logentry";
import {Observable} from "rxjs";
import {Player} from "../dto/player";

@Injectable({
  providedIn: 'root'
})
export abstract class AbstractLogService<L extends LogEntry, P extends Player> {

  protected constructor(translateService: TranslateService) {}

  abstract toStringRepresentation(l: L, players: Set<P>): Observable<string>;

}
