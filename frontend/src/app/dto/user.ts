import {UserRole} from "../enums/user-role";

export class User {
  constructor(public id: number,
              public username: string,
              public nickname: string,
              public email: string,
              public userRoles: UserRole[]) {
  }
}

export class UserRegisterRequest {
  constructor(public username: string,
              public password: string,
              public nickname: string,
              public email: string) {
  }
}

export class UserLoginRequest {
  constructor(public username: string,
              public password: string) {
  }
}

export class UserLoginResponse {
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
