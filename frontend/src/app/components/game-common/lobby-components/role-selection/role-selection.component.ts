import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Player} from "../../../../dto/player";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {FormControl} from "@angular/forms";
import {removeFromArray} from "../../../../utility/utility";

@Component({
  selector: 'app-role-selection',
  templateUrl: './role-selection.component.html',
  styleUrls: ['./role-selection.component.scss']
})
export class RoleSelectionComponent<R, P extends Player> implements OnInit {

  @Input() roles: R[];
  @Input() currentPlayer: P;
  @Input() mayEdit: (player: Player) => boolean;
  @Input() getArray: () => R[];

  @Output() roleChanged = new EventEmitter<R[]>();

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;

  public roleControl = new FormControl();

  constructor() {
  }

  ngOnInit(): void {
  }

  removeRole(
    role: R
  ): void {
    removeFromArray(role, this.roles)
    this.roleChanged.emit(this.roles);
  }

  selectAutocomplete(
    event: MatAutocompleteSelectedEvent
  ): void {
    const roleToAdd: R = event.option.value

    if (roleToAdd)
      this.roles.push(roleToAdd);

    this.roleInput.nativeElement.value = "";
    this.roleControl.setValue(null);
    this.roleChanged.emit(this.roles);
  }

  private filter(value: any): R[] {
    const filterValue = value.toLowerCase();

    return this.getArray().filter(role => (String(role)).includes(filterValue));
  }

  countRole(
    role: R
  ): number {
    let count = 0;

    for (let existingRole of this.roles) {
      if (existingRole == role)
        count++;
    }

    return count;
  }

}
