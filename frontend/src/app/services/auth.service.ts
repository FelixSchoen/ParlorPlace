import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {interval, Observable} from "rxjs";
import {AuthRequest, AuthResponse} from "../dto/authentication";
import {tap} from "rxjs/operators";
import {TokenService} from "./token.service";
import jwt_decode from "jwt-decode";
import {GlobalValues} from "../globals/global-values.service";
import {User, UserSigninRequest, UserSigninResponse, UserSignupRequest} from "../dto/user";

const AUTH_URI = GlobalValues.BASE_URI + 'user/';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authScheduler: Observable<number> = interval(1000);

  constructor(private httpClient: HttpClient, private tokenService: TokenService) {
    this.scheduleReAuthentication();
  }

  public signin(userSigninRequest: UserSigninRequest): Observable<UserSigninResponse> {
    this.tokenService.signout();
    return this.httpClient.post<UserSigninResponse>(AUTH_URI + 'signin', userSigninRequest, httpOptions)
      .pipe(
        tap((userSigninResponse: UserSigninResponse) => this.tokenService.saveToken(new AuthResponse(userSigninResponse.accessToken, userSigninResponse.refreshToken)))
      );
  }

  public signup(userSignupRequestDTO: UserSignupRequest): Observable<User> {
    return this.httpClient.post<User>(AUTH_URI + 'signup', userSignupRequestDTO, httpOptions);
  }

  public isSignedIn(): boolean {
    return !!this.tokenService.getToken() && this.getTokenExpirationDate(this.tokenService.getToken()!.accessToken)!.valueOf() > new Date().valueOf();
  }

  private scheduleReAuthentication() {
    this.authScheduler.subscribe(() => {
      if (this.isSignedIn()) {
        const timeLeft = this.getTokenExpirationDate(this.tokenService.getToken()!.accessToken)!.valueOf() - new Date().valueOf();
        if ((timeLeft / 1000) < 60) {
          this.reauthenticate().subscribe(
            () => {
              console.log('Re-authenticated successfully');
            }
          );
        }
      }
    });
  }

  reauthenticate(): Observable<AuthResponse> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + this.tokenService.getToken()?.refreshToken
      })
    };

    return this.httpClient.post<AuthResponse>(AUTH_URI + "refresh", httpOptions)
      .pipe(
        tap((authResponse: AuthResponse) => this.tokenService.saveToken(authResponse))
      );
  }

  private getTokenExpirationDate(token: string): Date | null {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCMilliseconds(decoded.exp);
    return date;
  }


}
