import {Component, OnInit, TemplateRef} from '@angular/core';
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit {
  notificationService: any;

  constructor(notificationService: NotificationService) {
    this.notificationService = notificationService;
  }

  ngOnInit(): void {
  }

}
