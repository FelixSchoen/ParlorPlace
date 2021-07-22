export class TokenRefreshRequest {
  constructor(
    public refreshToken: string
  ) {}
}

export class TokenRefreshResponse {
  constructor(
    public accessToken: string,
    public refreshToken: string
  ) {
  }
}
