import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Player} from "../../../../dto/player";
import {EnumMember} from "@angular/compiler-cli/src/ngtsc/reflection";

@Component({
  selector: 'app-resourcepack-selection',
  templateUrl: './resourcepack-selection.component.html',
  styleUrls: ['./resourcepack-selection.component.scss']
})
export class ResourcepackSelectionComponent<P extends Player, E extends EnumMember> implements OnInit {

  @Input() gameIdentifier: string;
  @Input() pack: E;
  @Input() currentPlayer: P;
  @Input() mayEdit: (player: Player) => boolean;
  @Input() getArray: E[];

  @Output() packChanged = new EventEmitter<E>();

  constructor() { }

  ngOnInit(): void {
  }

  public selectPack(event: any): void {
    this.packChanged.emit(event.value);
  }

}
