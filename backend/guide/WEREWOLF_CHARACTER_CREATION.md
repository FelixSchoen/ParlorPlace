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

Do not forget to add an entry in the `GameRoleClasses` map in `WerewolfGameService`:

```
private static final Map<WerewolfRoleType, Class<? extends WerewolfGameRole>> werewolfGameRoleClasses = new HashMap<>() {{
    put(WerewolfRoleType.ROLE, RoleWerewolfGameRole.class);
}};
```

Furthermore, matching `DTO`s and `WerewolfRoleType`s have to be created, in order to accommodate the new role.
A special case (not described in this guide) would be the creation of a new faction (say e.g. for the Lovers).
This should still be pretty straightforward.

Adjust the JSON mapping of the `WerewolfGameRoleDTO`s as follows:

```
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "werewolfRoleType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoleWerewolfGameRoleDTO.class, name = "ROLE"),
        [...]}
)
```

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

**Note**: This approach *could* potentially change in the future, as MapStruct perhaps will include an automatic approach for subtype mapping (see the corresponding [GitHub Issue](https://github.com/mapstruct/mapstruct/pull/2512)).

If the role stores information (such as e.g. if it has already performed an action), obfuscation needs to be added. For this, edit `WerewolfGameRoleObfuscationService`. The following snippet shows where to insert the obfuscation logic:

```
switch (werewolfGameRoleDTO.getWerewolfRoleType()) {
    case ROLE -> {
        [Remove sensitive information here]
    }
    [...]
}
```

### Game Moderator

Next up modify the game moderator. Start by creating new methods for the newly created role, e.g. like this:

```
    private void processRole(WerewolfPlayer role) {
      [Logic]
    }
```

It is important to note that this method must be included either in the `processNight()` or `processDay()` (or any equivalent) method, in order to be processed.

In order to create a new vote, `WerewolfVoteDescriptor`s have to be created as well.
Furthermore, if necessary, create additional `WerewolfLogType`s.

### Frontend

In order to adjust the frontend, we have to start by implementing the corresponding files to the ones just created for the backend.

Start by creating a new `WerewolfGameRole` in `werewolf.ts`. After that, adjust `WerewolfLogType`, `WerewolfRoleType`, `WerewolfVoteDescriptor`.

For `WerewolfLogType`, add a new case to the switch, like this:

```
switch (type) {
  case WerewolfLogType.ROLE_ACTION:
  iconString = "action";
  break;
```

For `WerewolfRoleType`, make sure to add an icon conforming to the string value of the role.
Icons are defined in `app.component.ts`.
Furthermore, add the role to the array in `WerewolfRoleTypeUtil`.

Furthermore, add fitting `WerewolfVoiceLineType`s - keep in mind that the actual voice line files have to be added as well, more on that later.

For localization, adjust `en.json` (and all equivalent files).
Add an entry in the `role` section, and entries for every newly created log type.

**Careful:** The key values must equal to the values given by the enumeration, as access to the localization fields is done in this way:

```
(this.getCurrentRoleTypeCurrentPlayer().valueOf() | internalRepresentation) + ".name") | translate
(this.getCurrentRoleTypeCurrentPlayer().valueOf() | internalRepresentation) + ".description") | translate
```

Similarly, for the vote descriptor be sure to fit the pattern determined by the following snippet (e.g. `SEER_SEE` would turn into `werewolf.vote.seer.see`):

```
{{getTranslationKey(vote.voteDescriptor) | translate}}

[...]

getTranslationKey(e: Object): string {
  return "werewolf.vote." + e.valueOf().toString().toLowerCase().replace("_", ".");
}
```

For the log types, edit `WerewolfLogService` to implement representations of the newly created logs like this:

```
case WerewolfLogType.ROLE:
        return this.translateService.get("werewolf.log." + textValue, {player: Player.toNameRepresentation(l.targets[0].user, players)});
```

Make sure not to mix up the `source` and `target` property in the backend, when sending logs.

#### Voicelines

For voicelines, edit `pack.json` and be sure to match the path given by the `WerewolfVoiceLineType`.
This works in similar fashion as above: Replace `_` by `.`, and insert the role voicelines under `voiceline.role`.
Be sure to adjust `getVoiceLine()` in `WerewolfResourcePack`, in order to match the voice line with the correct path (in case `voiceline.role` is not fitting for example).
