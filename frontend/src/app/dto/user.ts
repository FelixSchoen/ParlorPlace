import {UserRole} from "../enums/userrole";

export class User {
  constructor(public id: number,
              public username: string,
              public nickname: string,
              public email: string,
              public roles: UserRole[]) {
  }
}

export class UserSignupRequest {
  constructor(public username: string,
              public password: string,
              public nickname: string,
              public email: string) {
  }
}

export class UserSigninRequest {
  constructor(public username: string,
              public password: string) {
  }
}

export class UserSigninResponse {
  constructor(public id: number,
              public username: string,
              public roles: UserRole[],
              public accessToken: string,
              public refreshToken: string) {
  }
}
