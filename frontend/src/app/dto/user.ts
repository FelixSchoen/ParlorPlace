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

export class UserUpdateRequest {
  constructor(public id: number | null,
              public username: string | null,
              public password: string | null,
              public nickname: string | null,
              public email: string | null,
              public roles: UserRole[] | null) {
  }

}
