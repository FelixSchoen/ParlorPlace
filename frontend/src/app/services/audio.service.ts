import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AudioService {

  private audio;

  constructor() {
    this.audio = new Audio();
  }

  public playAudio(pathArray: string[]): void {
    let voiceline = pathArray.shift();
    if (voiceline != undefined) {
      this.audio.src = voiceline;
      this.audio.load();
      this.audio.play();
    }

    if (pathArray.length > 0) {
      this.audio.addEventListener("ended", () => {
        this.playAudio(pathArray);
      })
    }
  }

}
