import {VoiceLine, VoicePack} from "./voice-pack";

export class WerewolfVoicePack extends VoicePack {

  constructor() {
    super();
  }

  // The villagers win
  public villagersWin: VoiceLine[];
  // The werewolves win
  public werewolvesWin: VoiceLine[];
  // The lovers win
  public loversWin: VoiceLine[];

  // Village wakes up
  public villageWake: VoiceLine[];
  // Village falls asleep
  public villageSleep: VoiceLine[];

  // Werewolves wake up
  public werewolvesWake: VoiceLine[];
  // Werewolves fall asleep
  public werewolvesSleep: VoiceLine[];

  // Seer wakes up
  public seerWake: VoiceLine[];
  // Seer falls asleep
  public seerSleep: VoiceLine[];

  // Witch wakes up
  public witchWake: VoiceLine[];
  // Witch falls asleep
  public witchSleep: VoiceLine[];

  // Cupid wakes up
  public cupidWake: VoiceLine[];
  // Cupid falls asleep
  public cupidSleep: VoiceLine[];

  // Bodyguard wakes up
  public bodyguardWake: VoiceLine[];
  // Bodyguard falls asleep
  public bodyguardSleep: VoiceLine[];

  // Bear growls
  public shepherdGrowl: VoiceLine[];
  // Bear stay quiet
  public shepherdQuiet: VoiceLine[];

}
