export abstract class GameRole {
  protected constructor(public id: number) {
  }

  public abstract toIconRepresentation(): string;

}

