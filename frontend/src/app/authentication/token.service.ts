import {Injectable} from '@angular/core';
import {TokenRefreshResponse} from "../dto/authentication";

const ACCESS_TOKEN_KEY = 'auth-access-token';
const REFRESH_TOKEN_KEY = 'auth-refresh-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() {
  }

  public signout(): void {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }

  public saveToken(
    tokenRefreshResponse: TokenRefreshResponse
  ): void {
    localStorage.setItem(ACCESS_TOKEN_KEY, tokenRefreshResponse.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, tokenRefreshResponse.refreshToken);
  }

  public getToken(): TokenRefreshResponse | null {
    if (!!localStorage.getItem(ACCESS_TOKEN_KEY) && !!localStorage.getItem(REFRESH_TOKEN_KEY)) {
      return new TokenRefreshResponse(localStorage.getItem(ACCESS_TOKEN_KEY)!, localStorage.getItem(REFRESH_TOKEN_KEY)!);
    }
    return null;
  }

}
