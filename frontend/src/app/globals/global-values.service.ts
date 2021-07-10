import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class GlobalValues {
  public static BASE_URI: string = "http://localhost:8080/"
}
