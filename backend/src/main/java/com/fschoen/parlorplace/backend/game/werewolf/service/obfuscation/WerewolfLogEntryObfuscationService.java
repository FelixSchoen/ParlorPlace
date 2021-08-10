package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfLogEntryDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.DoubleObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WerewolfLogEntryObfuscationService extends DoubleObfuscationService<WerewolfLogEntryDTO, WerewolfGameDTO> {

    private final DoubleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> werewolfPlayerObfuscationService;

    @Autowired
    public WerewolfLogEntryObfuscationService(UserRepository userRepository,
                                              DoubleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> werewolfPlayerObfuscationService
    ) {
        super(userRepository);
        this.werewolfPlayerObfuscationService = werewolfPlayerObfuscationService;
    }

    @Override
    public WerewolfLogEntryDTO obfuscateFor(WerewolfLogEntryDTO werewolfLogEntryDTO, User user, WerewolfGameDTO werewolfGameDTO) {
        Set<WerewolfPlayerDTO> sources = new HashSet<>(this.werewolfPlayerObfuscationService.obfuscateFor(werewolfLogEntryDTO.getSources().stream().toList(), user, werewolfGameDTO));
        Set<WerewolfPlayerDTO> targets = new HashSet<>(this.werewolfPlayerObfuscationService.obfuscateFor(werewolfLogEntryDTO.getTargets().stream().toList(), user, werewolfGameDTO));
        return werewolfLogEntryDTO.toBuilder().sources(sources).targets(targets).build();
    }

    @Override
    public List<WerewolfLogEntryDTO> obfuscateFor(List<WerewolfLogEntryDTO> werewolfLogEntryDTOS, User user, WerewolfGameDTO werewolfGameDTO) {
        return super.obfuscateFor(werewolfLogEntryDTOS, user, werewolfGameDTO).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

}
