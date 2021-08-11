import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ParticipantListComponent} from './participant-list.component';

describe('PlayerListComponent', () => {
  let component: ParticipantListComponent<any>;
  let fixture: ComponentFixture<ParticipantListComponent<any>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParticipantListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipantListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
