export enum UserRole {
  USER = 'ROLE_USER', ADMIN = 'ROLE_ADMIN'
}

export class UserRoleUtil {

  public static getUserRoleArray(): UserRole[] {
    return [UserRole.USER, UserRole.ADMIN]
  }

  public static toStringRepresentation(role: UserRole): string {
    switch (role) {
      case UserRole.USER:
        return "User";
      case UserRole.ADMIN:
        return "Admin";
      default:
        return "Unknown";
    }
  }

}
