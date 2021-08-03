import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[gameHost]'
})
export class GameDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
