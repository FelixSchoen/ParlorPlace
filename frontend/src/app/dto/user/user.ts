import {UserRole} from "../../enums/userrole";

export class User {
  constructor(
    public username: string,
    public password: string,
    public nickname: string,
    public email: string,
    public roles: UserRole[]
  ) {}
}
