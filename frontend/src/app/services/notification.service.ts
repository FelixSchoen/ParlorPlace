import {Injectable, TemplateRef} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  public toasts: any[] = [];

  show(header: string, body: string, options: any = {}) {
    this.toasts.push({ header, body, ...options });
  }

  showStandard(message: string) {
    this.show("Notification", message);
  }

  showSuccess(message: string) {
    this.show("Success", message, { classname: 'bg-success text-light', delay: 10000 });
  }

  showError(message: string) {
    this.show("Error", message, { classname: 'bg-danger text-light', delay: 15000 });
  }

  remove(toast: any) {
    this.toasts = this.toasts.filter(t => t != toast);
  }

}
