import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private _snackBar: MatSnackBar) {
  }

  show(
    message: string,
    action: string,
    duration: number
  ) {
    this._snackBar.open(message, action, {
      duration: duration,
      panelClass: ['danger-snackbar']
    });
  }

  showSuccess(message: string,
              action: string = "Dismiss"
  ) {
    this.show(message, action, 5000)
  }

  showError(message: string,
            action: string = "Dismiss"
  ) {
    this.show(message, action, 15000)
  }

}
