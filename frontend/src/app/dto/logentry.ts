import {Player} from "./player";

export abstract class LogEntry {
  constructor(public identifier: string,
              public sources: Player[],
              public targets: Player[]) {
  }
}
