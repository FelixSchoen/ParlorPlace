# Changelog

- [v1.0 (2021-09-08)](#v1.0)
- [v1.0-alpha2 (2021-08-27)](#v1.0-alpha2)
- [v1.0-alpha1 (2021-08-25)](#v1.0-alpha1)
- [v0.4 (2021-08-16)](#v0.4)
- [v0.3 (2021-08-05)](#v0.3)
- [v0.2 (2021-07-22)](#v0.2)
- [v0.1 (2021-07-11)](#v0.1)

---

## v1.0 (27-09-08)
<a name="v1.0"></a>

## Added

- **Implemented hunter**
  - Has to kill another player upon their death
- **Implemented pure villager**
  - Is a normal villager with the addition that everyone knows that they are
- Deployed [ParlorPlace](https://parlorplace.fschoen.com)
- Missing and additional voicelines

## Fixed

- Error message on wrong login credentials

## Changed

- Updated document structure

---

## v1.0-alpha2 (27-08-2021)
<a name="v1.0-alpha2"></a>

## Added

- **Implemented witch**
  - New role: witch
  - Can heal and/or kill one player, each once per game
- **Implemented cupid**
  - New role: cupid
  - At the start of the game, links two players which then fall in love
  - If one of them dies, the other one dies as well
  - They cannot vote against each other in the lynching vote
- **Implemented bodyguard**
  - New role: bodyguard
  - Can protect one player each night, that cannot be killed by the werewolves
  - Cannot choose the same player on two consecutive nights
- **Implemented lycanthrope**
  - Is a normal villager
  - The seer sees them as a werewolf
- **Implemented bear tamer**
  - Each morning, the bear growls if there is at least one werewolf adjacent to the bear tamer
  - Otherwise it stays silent
- Missing voicelines

## Fixed

- Vote component with more than one selection
  - Previously, deselecting was not possible, and an exception would get thrown when selecting more than one option
- Calculation of vote outcome
- Winner determination

---

## v1.0-alpha1 (25-08-2021)
<a name="v1.0-alpha1"></a>

## Added

- **Finished vote framework**
- **Implemented werewolf game moderator**
  - Full games can be played for the roles that have been implemented
- Implemented post-game screen
  - Shows victors and losers of the game
  - Reveals all roles
- **Implemented voice lines**
  - Implemented audio player service
  - Added voice lines for all existing events
  - Implemented resource pack selection
- **Implemented seer**

---

## v0.4 (2021-08-16)
<a name="v0.4"></a>

## Added

- Combined role representation in frontend to combine duplicates
- Icons for seperate games
- **Game Moderator framework**
  - Added groundwork for the game moderator feature
  - Provided an interface for the start of games, in order to handle tasks such as assigning players to roles
- Added round and phase of game
- Added an option to see all current games
- Unique icons for different games
- Persistent test data for games
- **Vote framework**
  - One of the biggest changes to date: Added support for votes on objects of different kinds
  - Each vote can only be held on the same type of object
  - Only players are allowed to vote on choices
  - One can define the amount of votes each player has, and the options each player can vote on
  - Votes can also be abstained from, if the flag is set
- **Vote frontend framework**
  - Added a highly adaptable component that works for votes of different kinds

## Changed

- Players now have lists of roles rather than a single role
- Open lobbies that have not started are removed upon start of the backend
- Refactored obfuscation into its own service
- The frontend now combines several roles of the same type into one chip for better readability

---

## v0.3 (2021-08-05)
<a name="v0.3"></a>

## Added

- **Incorporated feedback**
- Deletion of games in lobby state on restart
- Retrieval of games a user is currently part of
  - Frontend visualization in order to be able to "re-join" a game
- **Communication from server to client**
  - Add Communication Service in both backend and frontend in order to update client on new information

## Changed

- Renamed sign-in to login and sign-up to register
- Deleted promise
- **Made entities generic, extend approach to services**
- Changed database folder
- **In-memory approach to persistence one**
- Optimized imports
- Route nesting
- Obfuscation outsourced to own service

## Fixed

- Persistence typos
- Game Service Prototype annotation

---

## v0.2 (2021-07-22)
<a name="v0.2"></a>

## Added

- Obtaining currently logged-in user
- Obtaining users by id and username
- Obtaining set of users filtered by username and nickname
- Auth guards for frontend
- **Profiles for users**
  - One can search for other users using the search bar at the top of the profile section
  - Profiles show nickname, username and roles of a user
- Sign out functionality
- **User editing functionality**
- **Web Socket security settings and framework**
- Dark mode (click the logo on the login page)
- **General game framework**
  - Implement joining of games
  - Implement changing of game lobbies
    - Implement changing of positions of players
    - Implement changing of rules of game
- Frontend creation and joining of games
- **Frontend lobby interface**
  - Changing positions of players per drag & drop
  - Changing ruleset (add roles)
- Added support for Continuous Integration

## Changed

- User update functionality now uses path variables
- **Frontend now makes use of Angular Material instead of Bootstrap, in order to use the chips module**

## Fixed

- Reauthentication procedure of frontend
- Current user retrieval in frontend
- Input fields not taking up full width

---

## v0.1 (2021-07-11)
<a name="v0.1"></a>

## Added

- Basic project structure for both frontend and backend
- **User account creation**
- User account sign in
- User account updates
- General project infrastructure like the wiki, readme and changelog files

---