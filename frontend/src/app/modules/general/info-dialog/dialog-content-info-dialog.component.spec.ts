import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DialogContentInfoDialog} from './dialog-content-info-dialog.component';

describe('InfoDialogComponent', () => {
  let component: DialogContentInfoDialog;
  let fixture: ComponentFixture<DialogContentInfoDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogContentInfoDialog ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogContentInfoDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
