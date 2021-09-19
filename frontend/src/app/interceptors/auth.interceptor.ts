import {Injectable} from "@angular/core";
import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {TokenService} from "../authentication/token.service";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {Router} from "@angular/router";
import {NotificationService} from "../services/notification.service";

const TOKEN_HEADER_KEY = 'Authorization';
const TOKEN_HEADER_PREFIX = 'Bearer '

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private tokenService: TokenService,
              private notificationService: NotificationService,
              private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;

    const token = this.tokenService.getToken()?.accessToken;

    if (token != null) {
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, TOKEN_HEADER_PREFIX + token)});
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {

        // Check if this is an authorization error
        if (error.error != undefined && error.error.error == "Unauthorized") {
          this.tokenService.signout();
          this.router.navigate([""]).then();
          this.notificationService.showError(error.error.message)
        }
        // Check if this is an input error
        else if (error.error) {
          this.notificationService.showError(error.error)
        }

        return throwError(error);
      })
    )

    // return next.handle(authReq).pipe(
    //   map((event: HttpEvent<any>) => {
    //     if (event instanceof HttpResponse) {
    //       if (event.body != undefined && event.body.error != undefined && event.body.error == "Unauthorized")
    //         this.tokenService.signout();
    //     }
    //     return event;
    //   })
    // );
  }

  handleError(error: HttpErrorResponse){
    console.log("lalalalalalalala");
    return throwError(error);
  }
}

export const authInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
];
