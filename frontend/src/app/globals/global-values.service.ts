import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class GlobalValues {
  public static BASE_URI: string = "http://localhost:8080/"
  public static ENTRY_URI: string= "entry/"
  public static PROFILE_URI: string = "profile/"
  public static GAME_URI: string = "game/"
}
