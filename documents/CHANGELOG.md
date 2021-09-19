# Changelog

- [v1.0.1 (2021-09-19)](#v1.0.1)
- [v1.0 (2021-09-08)](#v1.0)
- [v1.0-alpha2 (2021-08-27)](#v1.0-alpha2)
- [v1.0-alpha1 (2021-08-25)](#v1.0-alpha1)
- [v0.4 (2021-08-16)](#v0.4)
- [v0.3 (2021-08-05)](#v0.3)
- [v0.2 (2021-07-22)](#v0.2)
- [v0.1 (2021-07-11)](#v0.1)

---

## v1.0.1 (2021-09-xx)
<a name="v1.0.1"></a>

## Added

- Implemented fake service
  - Obfuscates certain actions, e.g. a witch that has already healed and killed will still be called

## Fixed

- Wrong seat position assignment upon quitting a lobby

## Changed

- Migrated to GitHub from GitLab
- Implemented performance improvements for frontend
- Made public votes observable by all
- Made all votes observable by all deceased players

---

## v1.0 (2021-09-08)
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

## v1.0-alpha2 (2021-08-27)
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

## v1.0-alpha1 (2021-08-25)
<a name="v1.0-alpha1"></a>

## Added
