export class Utility {

  public static removeFromArray(value: any, array: any[]): void {
    const index = array.indexOf(value);
    if (index >= 0) {
      array.splice(index, 1);
    }
  }

}
