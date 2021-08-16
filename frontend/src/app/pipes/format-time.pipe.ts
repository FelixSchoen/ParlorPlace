import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'formatTime',
  pure: false
})
export class FormatTimePipe implements PipeTransform {

  transform(value: number, ...args: unknown[]): string {
    const hours: number = Math.floor(value / 3600);
    const minutes: number = Math.floor((value % 3600) / 60);

    let timeString: string = "";

    if (hours > 0)
      timeString += ('00' + hours).slice(-2) + ':';
    if (minutes > 0 || timeString.length > 0)
      timeString += ('00' + minutes).slice(-2) + ':';
    timeString += ('00' + Math.floor(value - minutes * 60)).slice(-2);

    return timeString;
  }

}
