import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleSelectionComponent } from './role-selection.component';

describe('AddRoleComponent', () => {
  let component: RoleSelectionComponent;
  let fixture: ComponentFixture<RoleSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoleSelectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
