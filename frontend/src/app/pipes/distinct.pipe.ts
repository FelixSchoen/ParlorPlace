import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'distinct',
  pure: false
})
export class DistinctPipe implements PipeTransform {

  transform(value: any): any {
    return [...new Set(value)]
  }

}
