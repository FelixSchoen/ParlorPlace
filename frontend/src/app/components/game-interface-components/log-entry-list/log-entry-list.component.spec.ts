import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LogEntryListComponent} from './log-entry-list.component';

describe('LogEntryListComponent', () => {
  let component: LogEntryListComponent;
  let fixture: ComponentFixture<LogEntryListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LogEntryListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LogEntryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
