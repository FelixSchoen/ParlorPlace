import {Component, Input, OnInit, Output, EventEmitter, ViewChild, ElementRef} from '@angular/core';
import {Player} from "../../../dto/player";
import {Utility} from "../../../utility/utility";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-role-selection',
  templateUrl: './role-selection.component.html',
  styleUrls: ['./role-selection.component.scss']
})
export class RoleSelectionComponent<R, P extends Player> implements OnInit {

  @Input() roles: R[];
  @Input() currentPlayer: P;
  @Input() mayEdit: any;
  @Input() getArray: any;
  @Input() toStringRepresentation: any;

  @Output() roleChanged = new EventEmitter<R[]>();

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;

  public roleControl = new FormControl();

  constructor() { }

  ngOnInit(): void {
  }

  removeRole(
    role: R
  ): void {
    Utility.removeFromArray(role, this.roles)
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

}
