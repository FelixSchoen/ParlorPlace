import {UserRole} from "../../enums/userrole";

export class UserDTO {

  constructor(username: string | undefined, password: string | undefined, nickname: string | undefined, email: string | undefined, roles: UserRole[] | undefined) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.roles = roles;
  }

  public username: string | undefined;
  public password: string | undefined;
  public nickname: string | undefined;
  public email: string | undefined;
  public roles: UserRole[] | undefined;
}

export class UserSignupRequestDTO {
  constructor(public username: string,
              public password: string,
              public nickname: string,
              public email: string) {
  }
}
