import {Injectable, OnDestroy} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AudioService implements OnDestroy {

  private readonly audio;
  private pathArray: string[] = [];

  constructor() {
    this.audio = new Audio();
  }

  ngOnDestroy(): void {
    if (this.audio != undefined) {
      this.audio.pause();
      this.audio.currentTime = 0;
    }
  }

  public playAudio(pathArray: string[]): void {
    this.pathArray = this.pathArray.concat(pathArray);

    console.log(this.pathArray)

    if (this.audio.paused) {
      console.log("was paused")
      this.play()
    }
  }

  public play(): void {
    let voiceline = this.pathArray.shift();
    if (voiceline != undefined) {
      this.audio.src = voiceline;
      this.audio.load();
      this.audio.play();
    }

    this.audio.addEventListener("ended", () => {
      console.log("Ended playing: " + this.pathArray)
      console.log(this.pathArray)
      if (this.pathArray.length > 0) {
        this.play();
      }
    })
  }

}
