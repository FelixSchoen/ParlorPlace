import { Component, OnInit } from '@angular/core';
import {Stomp} from "@stomp/stompjs";

@Component({
  selector: 'app-experimental',
  templateUrl: './experimental.component.html',
  styleUrls: ['./experimental.component.css']
})
export class ExperimentalComponent implements OnInit {

  greetings: string[] = [];
  showConversation: boolean = false;
  ws: any;
  name: string;
  disabled: boolean;

  constructor(){}

  ngOnInit(): void {
  }

  connect() {
    //connect to stomp where stomp endpoint is exposed
    //let ws = new SockJS(http://localhost:8080/greeting);
    let socket = new WebSocket("ws://localhost:8080/greeting");
    this.ws = Stomp.over(socket);
    let that = this;
    this.ws.connect({}, function() {
      that.ws.subscribe("/errors", function(message: { body: string; }) {
        alert("Error " + message.body);
      });
      that.ws.subscribe("/topic/reply", function(message: { body: string; }) {
        console.log(message)
        that.showGreeting(message.body);
      });
      that.disabled = true;
    }, function(error: string) {
      alert("STOMP error " + error);
    });
  }

  disconnect() {
    if (this.ws != null) {
      this.ws.ws.close();
    }
    this.setConnected(false);
    console.log("Disconnected");
  }

  sendName() {
    let data = JSON.stringify({
      'name' : this.name
    })
    this.ws.send("/app/message", {}, data);
  }

  showGreeting(message: string) {
    this.showConversation = true;
    this.greetings.push(message)
  }

  setConnected(connected: boolean) {
    this.disabled = connected;
    this.showConversation = connected;
    this.greetings = [];
  }

}
