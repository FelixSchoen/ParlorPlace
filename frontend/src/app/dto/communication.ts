import {NotificationType} from "../enums/notification-type";
import {CodeName} from "../enums/code-name";

export class ClientNotification {
  constructor(public notificationType: NotificationType) {
  }
}

export abstract class VoiceLineClientNotification extends ClientNotification {
  protected constructor(public notificationType: NotificationType,
                        public codeNames: Set<CodeName>) {
    super(notificationType);
  }
}

export class WerewolfVoiceLineClientNotification extends VoiceLineClientNotification {
  protected constructor(public notificationType: NotificationType,
                        public codeNames: Set<CodeName>) {
    super(notificationType, codeNames);
  }
}
