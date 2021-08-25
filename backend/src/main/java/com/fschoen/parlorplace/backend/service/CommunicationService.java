package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import com.fschoen.parlorplace.backend.utility.communication.VoiceLineClientNotification;

import java.util.Set;

public interface CommunicationService {

    void sendNotification(GameIdentifier gameIdentifier, Set<User> recipients, NotificationType notificationType, Boolean primaryUri);

    void sendVoiceLineNotification(GameIdentifier gameIdentifier, Set<User> recipients, VoiceLineClientNotification notification);

}
