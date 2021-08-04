import {Injectable} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {TokenService} from "../authentication/token.service";
import {Observable} from "rxjs";

const TOKEN_HEADER_KEY = 'Authorization';
const TOKEN_HEADER_PREFIX = 'Bearer '

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private tokenService: TokenService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;

    const token = this.tokenService.getToken()?.accessToken;

    if (token != null) {
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, TOKEN_HEADER_PREFIX + token)});
    }

    return next.handle(authReq);
  }
}

export const authInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
];
