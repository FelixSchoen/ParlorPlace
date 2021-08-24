import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoadJsonService {

  constructor(private httpClient: HttpClient) { }

  public loadJson(path: string): Observable<any> {
    return this.httpClient.get(path);
  }

}
