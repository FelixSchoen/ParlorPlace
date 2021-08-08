package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.utility.communication.VoiceLineClientNotification;

import java.util.Set;

public interface CommunicationService {

    void sendGameStaleNotification(GameIdentifier gameIdentifier, Set<User> recipients);

    void sendLogsStaleNotification(GameIdentifier gameIdentifier, Set<User> recipients);

    void sendVoiceLineNotification(GameIdentifier gameIdentifier, Set<User> recipients, VoiceLineClientNotification notification);

}
