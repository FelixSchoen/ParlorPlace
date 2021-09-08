<div style="text-align:center">
<img src="./frontend/src/assets/image/ParlorPlace.svg" alt="ParlorPlace Logo" width="200"/>
</div>

# ParlorPlace
<a name="ParlorPlace"></a>

[ParlorPlace](https://parlorplace.fschoen.com) is a platform for social deduction games like the famous "Werewolf" (also known as "Mafia").
It is built to support relatively quick and easy addition of other social deduction games, like for example "The Resistance".

## Contents
<a name="Contents"></a>

- [ParlorPlace](#ParlorPlace)
  - [Contents](#Contents)
  - [Features](#Features)
  - [Guides](#Guides)
  - [Prerequisites](#Prerequisites)
  - [Changelog](#Changelog)

## Features
<a name="Features"></a>

#### User Account Management

Users are able to securely sign up for the service, using a username, a password and a valid e-mail address, which can be used in the future to recover access to an account.
Furthermore, they are able to securely log into their accounts.
Users are assigned different roles, according to which different actions are available to them (for instance, normal users and administrators are supposed to have different authority and make use of different functionality).

##### User Profiles

Each user has a profile, which shows essential information about them, for instance their nickname and their roles.
They can use this profile to edit their account (change their nickname, password and email) and sign out.
An administrator has the authority to edit not only his own account, but also the ones of others.

#### Game Management

Users are able to start ("host") and join existing games, using an identifier made up of at least 4 characters.
Doing so they will be placed in a game lobby, in which they can see the other participants and the rules of the game to be played.
The user who started the lobby will have additional privileges, like for example changing the seat position of the players, or editing the rules.
Furthermore this host user has a special icon next to them, indicating to all other players that they are indeed the host.
If a host leaves, another player of the lobby will be selected as host.
If no other player is left, the game will be deleted.

#### Playing games

Users are able to play the implemented games (at this point in time the social deduction game "Werewolf") using a simple and intuitive interface.
They only have to use their phone as a supplement to the overall gameplay, it is not supposed to distract them during the process of playing, but rather enhance it.
Logs provide additional information on what has already happened, and an information section provides them with further details about their role.
Using a voting component which is easily reusable for different types of votes they can make selections on different subjects.

#### Other

##### Extendability

ParlorPlace was built with extendability in mind; implementing new games or roles is an easy process and takes way less time than implementing these approaches from scratch!

##### Translation

ParlorPlace makes use of a central file that stores almost all the strings used.
Translating this file into your local language allows for new display languages!

## Guides
<a name="Guides"></a>

To get a better grasp of the application, and especially a visual representation of the different components, visit the following [Page](https://reset.inso.tuwien.ac.at/repo/praktika/ParlorPlace/-/wikis/Guide) in the wiki, detailing these aspects.

We provide you with a [Guide on how to create new Characters for Werewolf](documents/werewolf/ROLE_CREATION_GUIDE.md), which should serve as a starting point of creating new roles.
If you want to create a new role, we would ask you to create a pull request, which will then undergo closer examination.
Thank you for your help!

## Prerequisites
<a name="Prerequisites"></a>

- Open JDK 16.0.2 with Spring Boot 2.5.2
- Angular 12 with Material Design 12

## Changelog
<a name="Changelog"></a>

To see a changelog regarding the different versions of the application, see [Changelog](documents/CHANGELOG.md)

## Reports

To see the (bi-)weekly reports which cover the work done in this timeframe, visit the [Wiki](https://reset.inso.tuwien.ac.at/repo/praktika/ParlorPlace/-/wikis/Reports/Week28)
