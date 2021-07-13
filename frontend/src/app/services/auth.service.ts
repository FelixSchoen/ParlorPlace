import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {interval, Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {TokenService} from "./token.service";
import jwt_decode from "jwt-decode";
import {GlobalValues} from "../globals/global-values.service";
import {User, UserSigninRequest, UserSigninResponse, UserSignupRequest} from "../dto/user";
import {UserService} from "./user.service";
import {NotificationService} from "./notification.service";
import {TokenRefreshRequest, TokenRefreshResponse} from "../dto/authentication";

const AUTH_URI = GlobalValues.BASE_URI + 'user/';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authScheduler: Observable<number> = interval(1000);

  constructor(private httpClient: HttpClient, private tokenService: TokenService, private userService: UserService, private notificationService: NotificationService) {
    this.scheduleReAuthentication();
  }

  public signin(userSigninRequest: UserSigninRequest): Observable<UserSigninResponse> {
    this.tokenService.signout();
    return this.httpClient.post<UserSigninResponse>(AUTH_URI + 'signin', userSigninRequest, httpOptions)
      .pipe(
        tap((userSigninResponse: UserSigninResponse) => {
          this.tokenService.saveToken(new TokenRefreshResponse(userSigninResponse.accessToken, userSigninResponse.refreshToken));
        })
      );
  }

  public signup(userSignupRequestDTO: UserSignupRequest): Observable<User> {
    return this.httpClient.post<User>(AUTH_URI + 'signup', userSignupRequestDTO, httpOptions);
  }

  public isSignedIn(): boolean {
    return !!this.tokenService.getToken() && AuthService.getTokenExpirationDate(this.tokenService.getToken()!.accessToken)!.valueOf() > new Date().valueOf();
  }

  private scheduleReAuthentication() {
    this.authScheduler.subscribe(() => {
      if (this.isSignedIn()) {
        const timeLeft = AuthService.getTokenExpirationDate(this.tokenService.getToken()!.accessToken)!.valueOf() - new Date().valueOf();
        if ((timeLeft / 1000) < 2 * 60) {
          this.reauthenticate().subscribe(
            () => console.log("Re-authenticated successfully"),
            () => console.log("Could not re-authenticate")
          );
        }
      }
    });
  }

  reauthenticate(): Observable<TokenRefreshResponse> {
    return this.httpClient.post<TokenRefreshResponse>(AUTH_URI + "refresh", new TokenRefreshRequest(this.tokenService.getToken()!.refreshToken)).pipe(
      tap((tokenRefreshResponse: TokenRefreshResponse) => this.tokenService.saveToken(tokenRefreshResponse))
    );
  }

  private static getTokenExpirationDate(token: string): Date | null {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }


}
