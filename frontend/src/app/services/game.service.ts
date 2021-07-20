import { Injectable } from '@angular/core';
import {GlobalValues} from "../globals/global-values.service";
import {HttpClient} from "@angular/common/http";

const USER_URI = GlobalValues.BASE_URI + 'game/';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private httpClient: HttpClient) {
  }




}
