import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'internalRepresentation'
})
export class InternalRepresentationPipe implements PipeTransform {

  transform(value: string): unknown {
    return value.toLowerCase().replace(' ', '_');
  }

}
