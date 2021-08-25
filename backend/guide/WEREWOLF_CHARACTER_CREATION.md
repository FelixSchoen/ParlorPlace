# Character Creation Guide

The purpose of this guide is to serve as a future reference on creating new characters for the game Werewolf on ParlorPlace.

### Role File Creation

Start by creating the necessary files for the new role (in the following referred to as `Role`).
This excerpt shows an example class representing a new role:

```
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  @Data
  @Entity
  public class RoleWerewolfGameRole extends WerewolfGameRole {

      @Column(nullable = false)
      @Builder.Default
      @NotNull
      private WerewolfRoleType werewolfRoleType = WerewolfRoleType.ROLE;

      @Column(nullable = false)
      @Builder.Default
      @NotNull
      private WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

  }
```

Furthermore, matching `DTO`s and `WerewolfRoleType`s have to be created, in order to accommodate the new role.
A special case (not described in this guide) would be the creation of a new faction (say e.g. for the Lovers).
This should still be pretty straightforward.

The next step is to modify the `WerewolfGameRoleMapper`. Add new entries similar to the code shown below:

```
RoleWerewolfGameRoleDTO toDTO(RoleWerewolfGameRole gameRole);

default WerewolfGameRoleDTO toDTO(WerewolfGameRole gameRole) {
    switch (gameRole.getWerewolfRoleType()) {
        case ROLE -> {
            return toDTO((RoleWerewolfGameRole) gameRole);
        }
        [...]
    }
}
```

Do the same for the other direction:

```
RoleWerewolfGameRole fromDTO(RoleWerewolfGameRoleDTO gameRole);

default WerewolfGameRole fromDTO(WerewolfGameRoleDTO gameRoleDTO) {
    switch (gameRoleDTO.getWerewolfRoleType()) {
        case ROLE -> {
            return fromDTO((RoleWerewolfGameRoleDTO) gameRoleDTO);
        }
        [...]
    }
}
```
