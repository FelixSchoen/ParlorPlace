import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WerewolfVoteComponent } from './werewolf-vote.component';

describe('WerewolfVoteComponent', () => {
  let component: WerewolfVoteComponent;
  let fixture: ComponentFixture<WerewolfVoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WerewolfVoteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WerewolfVoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
