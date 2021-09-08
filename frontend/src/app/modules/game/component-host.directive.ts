import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[componentHost]'
})
export class ComponentHost {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
