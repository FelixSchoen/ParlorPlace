import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";

export const TITLE = environment.general.parlorPlace;

@Injectable({
  providedIn: 'root'
})
export class TabTitleService {

  constructor() {
  }

  public setNotification(amount: number) {
    if (amount <= 0) {
      document.title = TITLE;
    } else {
      document.title = "(" + amount + ") " + TITLE;
    }
  }

  public clearNotification() {
    this.setNotification(0);
  }

}
