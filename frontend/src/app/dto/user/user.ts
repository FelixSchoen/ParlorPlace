import {UserRole} from "../../enums/userrole";

export class User {
  public username: string | undefined;
  public password: string | undefined;
  public nickname: string | undefined;
  public email: string | undefined;
  public roles: UserRole[] | undefined;
}
