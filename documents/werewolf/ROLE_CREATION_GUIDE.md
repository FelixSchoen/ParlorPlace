# Character Creation Guide

Thanks for considering to implement a new role for the Werewolf aspect of *ParlorPlace*!
This guide is supposed to provide you with a quick start to the process of doing so.

If you are already familiar with the process you can consider skipping the detailed instructions and rather jump to the [Checklist](#checklist), which only provides you with some bullet points to keep in mind when creating new roles.

## Backend

This section covers everything regarding the backend, and should serve as your starting point.
It is very much advised to start working on the backend, since the frontend can be adapted rather quickly.

Over the entire course of this guide we will use `XYZ` as a placeholder for the name of the new role, to provide you with more intuition of the naming conventions, and which part of the names to change.

### Role File Creation

We start by creating the initial entity and DTO files for the new role, which are located under `game/werewolf/entity/gamerole` and `game/werewolf/dto/gamerole` respectively.
The following snippet shows an example class representing a new role.
Here you can and should add variables to keep track of operations and actions the role has already taken (e.g. if the witch has already healed, the cupid has already linked two players, ...).
Keep in mind that the application is written with special cases in mind such as players changing roles, thus you cannot assume that a character has already taken an action only if it is no longer the first round.

```
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class XYZWerewolfGameRole extends WerewolfGameRole {

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    private WerewolfRoleType werewolfRoleType = WerewolfRoleType.XYZ;

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    private WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

}
```

The process for creating a DTO is more or less the same, simply make sure to follow the approach given by the already existing DTOs.
The following snippet should provide you with additional information about how a DTO should look.

```
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class XYZWerewolfGameRoleDTO extends WerewolfGameRoleDTO {

    @Builder.Default
    @NotNull
    private WerewolfRoleType werewolfRoleType = WerewolfRoleType.XYZ;

    @Builder.Default
    @NotNull
    private WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

}
```

At this point you will probably (and should get) warnings that your `WerewolfRoleType`s cannot be found, since we have not created them yet.
Open `WerewolfRoleType` and add a new entry according to the role you want to create.
Note that ideally roles only consist of a single word, although *ParlorPlace* supports roles with two or more words as their name as well.
At the moment, role names consisting of three or more words (internally) are **not** supported - this is due to complications with looking these roles up in the JSON files of the frontend, and uncertainty of how to do so.

Note that this guide does not consider the creation of new Factions (such as Vampires, ...), as this is a more advanced topic and would not fit within its bounds.

It is important to adjust the JSON mapping of `WerewolfGameRoleDTO` as follows, as otherwise the application would not know how to map responses from the frontend:

```
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "werewolfRoleType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = XYZWerewolfGameRoleDTO.class, name = "XYZ"),
        [...]}
)
```

Furthermore, do not forget to add an entry in the `GameRoleClasses` map in `WerewolfGameService`, as this map is used to instantiate new roles when creating a game.
This together with the above JSON mapping is a common point of bugs, so make sure to correctly define the entries here.
Failure to do so will result in wrongly created roles and errors.

```
private static final Map<WerewolfRoleType, Class<? extends WerewolfGameRole>> werewolfGameRoleClasses = new MapBuilder<WerewolfRoleType, Class<? extends WerewolfGameRole>>()
        .put(WerewolfRoleType.XYZ, XYZWerewolfGameRole.class)
        [...].build();
```

The next step is to modify the `WerewolfGameRoleMapper`, which is responsible of translate between the DTOs and entity objects.
Add new entries similar to the code shown below.
Note that this has to be done for the other direction (DTO to entry) as well, but the code for that part will not be shown here, as this would simply be repetitive.

```
XYZWerewolfGameRoleDTO toDTO(XYZWerewolfGameRole gameRole);

default WerewolfGameRoleDTO toDTO(WerewolfGameRole gameRole) {
    switch (gameRole.getWerewolfRoleType()) {
        case XYZ -> {
            return toDTO((XYZWerewolfGameRole) gameRole);
        }
        [...]
    }
}
```

**Note**: This approach *could* potentially change in the future, as MapStruct perhaps will include an automatic approach for subtype mapping (see the corresponding [GitHub Issue](https://github.com/mapstruct/mapstruct/pull/2512)).

If the role stores information (such as e.g. if it has already performed an action), obfuscation (meaning removing this information for other players that are not supposed to see it) needs to be added.
The class `WerewolfGameRoleObfuscationService` handles any and all obfuscation when it comes to the roles.
The following snippet shows where to insert the obfuscation logic.
Use this to remove information that is only stored for your role, e.g. whether your role has already taken an action.

```
switch (werewolfGameRoleDTO.getWerewolfRoleType()) {
    case XYZ -> {
        [Remove sensitive information here]
    }
    [...]
}
```

### Game Moderator

The next step consists of modifying the game moderator, and adding the actual logic to your role.
This will probably be the most complicated part, and will differt greatly from role to role, which is why we can only provide some pointers in this guide.
Start by creating new methods for the newly created role, e.g. like this:

```
    private void processXYZ(WerewolfPlayer xyz) {
      [Logic]
    }
```

It is important to note that this method must be included either in the `processNight()` or `processDay()` (or any equivalent) method, in order to be processed.
Per convention of this application you should also provide a `processAllXYZ()` (or similar) method, in order to encapsulate the retrieval of all eligible players from the actual processing of them.
For this step, it can be of great help to have a look at already implemented roles, such as the Seer for example.

Some pointers we can give is to use `processNightPreVote()` and `processNightPostVote()` which are supposed to handle some common workloads (waking up and telling the players to fall asleep again) for roles which wake up at night.

Note that at this point you will probably have to create new entries for `WerewolfVoiceLineType`.
Try to follow the nomenclature given by the existing voice line types.
These are only used to play the correct voice lines in the frontend.

In order to create a new vote, a new `WerewolfVoteDescriptor` has to be created as well.
The vote framework is designed in such a way as to make creating new votes (on the same type of subjects, e.g. other players) as easy as possible, for this you only need to call the method `requestVote()` of the local `playerVoteService`.
Note that if you need to create a vote on a different type of subject (such as a yes / no selection) this will entail a bit of work, as a new type of vote has to be created.

Lastly, if you need them create one or more `WerewolfLogType`s, which will be used in the frontend to provide the users with additional information.
At the moment these log types come with the functionality to include target and source players (the split into these two groups is rather arbitrary and most of the time dependant on semantics).
It should be possible to create a new log type that includes additional information, but this will also not be covered in this basic guide.

## Frontend

In order to adjust the frontend, we have to start by implementing the corresponding files to the ones just created for the backend.

Start by creating a new `WerewolfGameRole` in `werewolf.ts`.
This is a rather straightforward process, as you simply have to create an object that conform´s to the DTO created in the backend.

After that, adjust the enumerations that you have modified in the backend.

Add the new role type to the enumeration `WerewolfRoleType` in `werewolf-role-type.ts`, and make sure also add it to the `getArray()` method of `WerewolfRoleTypeUtil` in the same class, as this will be used to provide a user with the option of your new role.
Furthermore, add a new icon for your role under `assets/icon(/duotone)`.
The icons used for this project stem from [Font Awesome](https://fontawesome.com/), as should the icon you choose for your new role.
The icon also needs to be defined in `app.component.ts`, in a similar fashion to the other icons - this is a very straightforward process.

Add all needed entries for `WerewolfLogType`, and make sure to provide a new case to the switch, as shown below.
This ensures that the log has the correct icon.
This way you can define which icon to use for your newly created log type.

```
switch (type) {
  case WerewolfLogType.ROLE_ACTION:
  iconString = "action";
  break;
```

Lastly, add fitting `WerewolfVoiceLineType`s - keep in mind that the actual voice line files have to be added as well, more on that later.

For localization, adjust `en.json` (and all equivalent localization files, which may or may not exist).
Add an entry in the `role` section, and entries for every newly created log type.

**Careful:** The key values must equal to the values given by the enumeration, as access to the localization fields is done in this way:

```
("werewolf.role." + this.werewolfRoleTypeUtil.toInternalRepresentation(this.getCurrentRoleTypeCurrentPlayer()) + ".name") | translate
("werewolf.role." + this.werewolfRoleTypeUtil.toInternalRepresentation(this.getCurrentRoleTypeCurrentPlayer()) + ".description") | translate
```

where

```
public static toInternalRepresentation(type: WerewolfRoleType): string {
  let parts: string[] = type.toLowerCase().split("_")
  let finalString: string = "" + parts.shift();

  for (let part of parts) {
    finalString += part.charAt(0).toUpperCase() + part.slice(1);
  }

  return finalString;
}
```

This means that e.g. "SEER" will be turned into "seer", while "BEAR_TAMER" will be turned into "bearTamer".
Keep this in mind when naming your JSON fields, because otherwise the strings will not be found by the application.

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

Use this service to correctly format your logs using the fields it contains.
Make sure not to mix up the `source` and `target` property in the backend, when sending logs.

#### Voicelines

For voicelines, edit `pack.json` and be sure to match the path given by the `WerewolfVoiceLineType`.
This works in similar fashion as above: Replace `_` by `.`, and insert the role voicelines under `voiceline.role`.
Be sure to adjust `getVoiceLine()` in `WerewolfResourcePack`, in order to match the voice line with the correct path (in case `voiceline.role` is not fitting for example).
Lastly, you have to add the actual voice line files under `assets/resourcepack/werewolf/[packname]`, although this is not essential for the correct operation of the program and only serves as a method to inform the players of the game progress.
For testing purposes you can run without these files in place, as they will simply be skipped.

## Checklist
<a name="checklist"></a>

### Backend

- Create entity file
- Create DTO files
- Create `WerewolfRoleType` entry
- Edit JSON mapping in `WerewolfGameRoleDTO`
- Add entry to `GameRoleClasses` in `WerewolfGameService`
- Modify `WerewolfGameRoleMapper`
- Add logic to game moderator
  - Possibly add new `WerewolfVoteDescriptor`
  - Possibly add new `WerewolfLogType`
  - Possibly add new `WerewolfVoiceLineType`
  - ...

### Frontend

- Create `WerewolfGameRole` entry
- Create `WerewolfRoleType` entry and add to `getArray()` method
- Add icon and declare in `app.component.ts`
- Add needed entries for `WerewolfLogType` and provide case for switch
- Add needed entries for `WerewolfVoiceLineType`
- Adjust `en.json`
  - Add entry in the roles section
  - Add entry for log types
- Edit `WerewolfLogService` to implement representation of logs
- Edit `pack.json` and define new voice lines
