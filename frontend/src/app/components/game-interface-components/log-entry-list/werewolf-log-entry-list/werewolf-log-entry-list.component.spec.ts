import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WerewolfLogEntryListComponent} from './werewolf-log-entry-list.component';

describe('WerewolfLogEntryListComponent', () => {
  let component: WerewolfLogEntryListComponent;
  let fixture: ComponentFixture<WerewolfLogEntryListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WerewolfLogEntryListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WerewolfLogEntryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
