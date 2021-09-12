import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Player} from "../../../../dto/player";
import {MatAutocompleteSelectedEvent, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {FormControl} from "@angular/forms";
import {removeFromArray} from "../../../../utility/utility";
import {Observable, of} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {debounceTime, distinctUntilChanged, startWith, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-role-selection',
  templateUrl: './role-selection.component.html',
  styleUrls: ['./role-selection.component.scss']
})
export class RoleSelectionComponent<R, P extends Player> implements OnInit {

  @Input() roles: R[];
  @Input() currentPlayer: P;
  @Input() mayEdit: (player: Player) => boolean = () => false;
  @Input() getArray: R[];

  @Input() translationPrefix: string;
  @Input() internalRepresentation: (r: R) => string;

  @Output() roleChanged = new EventEmitter<R[]>();

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;
  @ViewChild('trigger', { read: MatAutocompleteTrigger }) trigger: MatAutocompleteTrigger;

  public roleControl = new FormControl();
  public filteredOptions: Observable<R[]>;

  constructor(
    private translateService: TranslateService
  ) {
  }

  ngOnInit(): void {
    this.filteredOptions = this.roleControl.valueChanges.pipe(
      startWith(""),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => term ? term.length < 1 ? of(this.getArray) : this.getRolesFiltered(term) : of(this.getArray))
    )
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
    this.roleControl.setValue("");
    this.roleChanged.emit(this.roles);

    let that = this;
    requestAnimationFrame(function () {
      that.trigger.openPanel();
    })
  }

  displayRole(
    role: R
  ): Observable<string> {
    return this.translateService.get((this.translationPrefix + this.internalRepresentation(role) + ".name"))
  }

  private getRolesFiltered(
    value: any
  ): Observable<R[]> {
    const filterValue = value.toLowerCase();
    return of(this.getArray.filter(role => this.translateService.instant((this.translationPrefix + this.internalRepresentation(role) + ".name")).toLowerCase().includes(filterValue)))
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

  incrementRole(
    role: R
  ) {
    this.roles.push(role);
    this.roleChanged.emit(this.roles);
  }

}
