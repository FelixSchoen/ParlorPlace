export class AuthRequest {
  constructor(
    public username: string,
    public password: string
  ) {}
}

export class AuthResponse {
  constructor(
    public accessToken: string,
    public refreshToken: string
  ) {
  }
}
