import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";

export interface DialogData {
  iconSelector: string;
  titleKey: string;
  descriptionKey: string;
}

@Component({
  selector: 'app-info-dialog',
  templateUrl: './dialog-content-info-dialog.component.html',
  styleUrls: ['./dialog-content-info-dialog.component.scss']
})
export class DialogContentInfoDialog implements OnInit {

  constructor(public dialogRef: MatDialogRef<DialogContentInfoDialog>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData, public formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
  }

  onClose(): void {
    this.dialogRef.close(this.data);
  }

}
