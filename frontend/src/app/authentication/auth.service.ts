import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, timer} from "rxjs";
import {tap} from "rxjs/operators";
import {TokenService} from "./token.service";
import jwt_decode from "jwt-decode";
import {User, UserSigninRequest, UserSigninResponse, UserSignupRequest} from "../dto/user";
import {TokenRefreshRequest, TokenRefreshResponse} from "../dto/authentication";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";

const AUTH_URI = environment.BASE_URI + environment.general.USER_API;
const BUFFER_SECONDS = 120;

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private tokenService: TokenService, private router: Router) {
    this.scheduleReAuthentication();
  }

  public signin(
    userSigninRequest: UserSigninRequest
  ): Observable<UserSigninResponse> {
    this.tokenService.signout();
    return this.httpClient.post<UserSigninResponse>(AUTH_URI + 'signin', userSigninRequest, httpOptions)
      .pipe(
        tap((userSigninResponse: UserSigninResponse) => {
          this.tokenService.saveToken(new TokenRefreshResponse(userSigninResponse.accessToken, userSigninResponse.refreshToken));
        })
      );
  }

  public signup(
    userSignupRequestDTO: UserSignupRequest
  ): Observable<User> {
    return this.httpClient.post<User>(AUTH_URI + 'signup', userSignupRequestDTO, httpOptions);
  }

  public isSignedIn(): boolean {
    return !!this.tokenService.getToken() && AuthService.getTokenExpirationDate(this.tokenService.getToken()!.accessToken)!.valueOf() > new Date().valueOf();
  }

  private scheduleReAuthentication() {
    let reauthenticateTimer: Observable<number> = this.setupTimer();
    let subscription = reauthenticateTimer.subscribe({
      next: () => {
        if (this.isSignedIn()) {
          this.reauthenticate().subscribe({
              next: () => {
                console.log("Re-authenticated successfully")
                this.scheduleReAuthentication();
              },
              error: () => {
                console.log("Could not re-authenticate");
                this.tokenService.signout();
                this.router.navigate([environment.general.ENTRY_URI]).then();
              }
            }
          ).add(() => subscription.unsubscribe())
          ;
        }
      }
    });
  }

  private setupTimer(): Observable<number> {
    let timeLeft = 0;
    if (this.tokenService.getToken() !== undefined && this.tokenService.getToken()?.accessToken !== undefined)
      timeLeft = AuthService.getTokenExpirationDate(this.tokenService.getToken()!.accessToken)!.valueOf() - new Date().valueOf();

    timeLeft = timeLeft - BUFFER_SECONDS * 1000;
    timeLeft = Math.max(1000, timeLeft);

    return timer(timeLeft);
  }

  private reauthenticate(): Observable<TokenRefreshResponse> {
    return this.httpClient.post<TokenRefreshResponse>(AUTH_URI + "refresh", new TokenRefreshRequest(this.tokenService.getToken()!.refreshToken)).pipe(
      tap((tokenRefreshResponse: TokenRefreshResponse) => this.tokenService.saveToken(tokenRefreshResponse))
    );
  }

  private static getTokenExpirationDate(
    token: string
  ): Date | null {
    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }


}
