import {Injectable} from '@angular/core';
import {TokenRefreshResponse} from "../dto/authentication";
import {User} from "../dto/user";

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

  public saveToken(tokenRefreshResponse: TokenRefreshResponse): void {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.setItem(ACCESS_TOKEN_KEY, tokenRefreshResponse.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, tokenRefreshResponse.refreshToken);
  }

  public getToken(): TokenRefreshResponse | null {
    if (!!localStorage.getItem(ACCESS_TOKEN_KEY) && !!localStorage.getItem(REFRESH_TOKEN_KEY)) {
      return new TokenRefreshResponse(localStorage.getItem(ACCESS_TOKEN_KEY)!, localStorage.getItem(REFRESH_TOKEN_KEY)!);
    }
    return null;
  }

  public saveUser(user: User): void {
    localStorage.removeItem(USER_KEY);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): User | null {
    const user = localStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return null;
  }

}
