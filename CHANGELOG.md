# Changelog

- [v0.1 (2021-07-11)](#v0.1)
- [v0.2 (2021-07-22)](#v0.2)
- [v0.3 (2021-08-xx)](#v0.3)

---

## v0.3
<a name="v0.3"></a>

## Added

- **Incorporated feedback**
- Deletion of games in lobby state on restart

## Changed

- Renamed sign-in to login and sign-up to register
- Deleted promise
- **Made entities generic, extend approach to services**
- Changed database folder
- **In-memory approach to persistence one**
- Optimized imports
- Route nesting

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
