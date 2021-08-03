import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {User, UserUpdateRequest} from "../dto/user";
import {UserRole} from "../enums/userrole";
import {environment} from "../../environments/environment";

const USER_URI = environment.BASE_URI + environment.general.USER_API;

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) {
  }

  public updateUser(
    id: number,
    userUpdateRequest: UserUpdateRequest
  ): Observable<User> {
    return this.httpClient.put<User>(USER_URI + "update/" + id, userUpdateRequest);
  }

  public getCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(USER_URI + "individual");
  }

  public getUserById(
    id: number
  ): Observable<User> {
    return this.httpClient.get<User>(USER_URI + "individual/" + id)
  }

  public getUserByUsername(
    username: string
  ): Observable<User> {
    return this.httpClient.get<User>(USER_URI + "individual/username/" + username);
  }

  public getAllUsersFiltered(
    username: string | null,
    nickname: string | null): Observable<User[]> {
    let params = new HttpParams();
    if (username != null)
      params = params.append('username', username);
    if (nickname != null)
      params = params.append('nickname', nickname);

    return this.httpClient.get<User[]>(USER_URI, {params: params});
  }

  public isAdmin(
    user: User
  ): boolean {
    if (user.userRoles == null) return false;
    return user.userRoles.includes(UserRole.ADMIN);
  }

}
