import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WerewolfInterfaceComponent} from './werewolf-interface.component';

describe('WerewolfInterfaceComponent', () => {
  let component: WerewolfInterfaceComponent;
  let fixture: ComponentFixture<WerewolfInterfaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WerewolfInterfaceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WerewolfInterfaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
