import {NotificationType} from "../enums/notification-type";
import {CodeName} from "../enums/code-name";
import {WerewolfVoiceLineType} from "../enums/games/werewolf-voice-line-type";
import {EnumMember} from "@angular/compiler-cli/src/ngtsc/reflection";

export class ClientNotification {
  constructor(public notificationType: NotificationType) {
  }
}

export abstract class VoiceLineClientNotification extends ClientNotification {
  protected constructor(public notificationType: NotificationType,
                        public codeNames: Set<CodeName>,
                        public voiceLineType: string) {
    super(notificationType);
  }
}

export class WerewolfVoiceLineClientNotification extends VoiceLineClientNotification {
  protected constructor(public notificationType: NotificationType,
                        public codeNames: Set<CodeName>,
                        public voiceLineType: WerewolfVoiceLineType) {
    super(notificationType, codeNames, voiceLineType);
  }
}
