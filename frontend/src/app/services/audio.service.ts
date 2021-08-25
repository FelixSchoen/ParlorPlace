import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
// TODO OnDestroy implementation
export class AudioService {

  private pathQueue: string[][] = [];
  private startable: boolean = true;

  constructor() {
  }

  public queueAudio(pathArray: string[]): void {
    this.pathQueue.push(pathArray);

    if (this.startable) {
      this.startNextInQueue();
    }
  }

  private startNextInQueue() {
    let path: string[] | undefined = this.pathQueue.shift();
    if (path != undefined) {
      let audio = new Audio();
      this.startable = false;
      this.play(audio, path);
    }
  }

  private play(audio: HTMLAudioElement, pathArray: string[]) {
    let voiceline = pathArray.shift();

    if (voiceline != undefined) {
      audio.src = voiceline;
      audio.load();
      audio.play().then();

      audio.addEventListener("ended", () => {
        if (pathArray.length > 0) {
          this.play(audio, pathArray);
        } else if (this.pathQueue.length > 0) {
          this.startNextInQueue();
        } else {
          this.startable = true;
        }
      })
    }
  }

}
