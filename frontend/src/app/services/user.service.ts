import {Injectable, OnInit} from '@angular/core';
import {GlobalValues} from "../globals/global-values.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {User} from "../dto/user";
import {NotificationService} from "./notification.service";
import {tap} from "rxjs/operators";

const USER_URI = GlobalValues.BASE_URI + 'user/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient, private notificationService: NotificationService) {
  }

  public getCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(USER_URI + "individual");
  }

  public getUserById(id: number): Observable<User> {
    return this.httpClient.get<User>(USER_URI + "individual/" + id)
  }

  public getUserByUsername(username: string): Observable<User> {
    return this.httpClient.get<User>(USER_URI + "individual/username/" + username);
  }

  public getAllUsersFiltered(username: string | null, nickname: string | null) {
    let params = new HttpParams();
    if (username != null)
      params = params.append('username', username);
    if (nickname != null)
      params = params.append('nickname', nickname);

    return this.httpClient.get<Set<User>>(USER_URI, {params: params});
  }

}
