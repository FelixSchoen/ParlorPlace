import {Component, OnInit} from '@angular/core';
import {Stomp} from "@stomp/stompjs";
import {TokenService} from "../../authentication/token.service";

@Component({
  selector: 'app-experimental',
  templateUrl: './experimental.component.html',
  styleUrls: ['./experimental.component.css']
})
export class ExperimentalComponent implements OnInit {

  public ws: any;
  public sessionId = "";
  public url = "ws://localhost:8080/communication/game";

  constructor(private tokenService: TokenService) {
    this.headers.Authentication = this.headers.Authentication + this.tokenService.getToken()?.accessToken;
  }

  ngOnInit(): void {

  }

  headers = {
    Authentication: "Bearer "
  };

  connect() {
    let socket = new WebSocket(this.url);
    this.ws = Stomp.over(socket);

    let that = this;
    this.ws.connect(this.headers, function() {
      that.ws.subscribe("/user/queue/game/TEST", function(message: any) {
        console.log(message.body)
      });

    }, function (error: string) {
      alert("STOMP error " + error);
    });
  }

  disconnect() {
    if (this.ws != null) {
      this.ws.ws.close();
    }
    console.log("Disconnected");
  }

  send(message: String) {
    let data = JSON.stringify({
      'name': message
    })
    this.ws.send("/ws/wss/exp", {}, data);
  }

}
