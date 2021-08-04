import {Injectable} from '@angular/core';
import {TokenService} from "../authentication/token.service";
import {CompatClient, Stomp} from "@stomp/stompjs";

@Injectable({
  providedIn: 'root'
})
export class CommunicationService {

  private headers = {
    Authentication: "Bearer "
  };

  constructor(private tokenService: TokenService) {
  }

  public connectSocket(url: string, subscribeUrl: string, subscribeCallback: (payload: any) => any) {
    this.headers.Authentication += this.tokenService.getToken()?.accessToken;

    let client = Stomp.client(url);
    client.brokerURL = url;

    // Disables console messages
    client.debug = () => {};

    client.connect(this.headers,
      function () {
        client.subscribe(subscribeUrl, subscribeCallback);
      }, function (error: string) {
        console.error("Error with client communication: ", error);
      });

    return client;
  }

  public disconnectSocket(client: CompatClient) {
    client.webSocket.close();
  }


}
