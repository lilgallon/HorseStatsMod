## Version 1.4.9 for Minecraft 1.21

- Add korean translation (thanks @smoong951)

## Version 1.4.8 for Minecraft 1.21

- Accurate horse blocks/second speed (thanks @Armakatsu)
  - factor was updated from 43 to 42.16

## Version 1.4.7 for Minecraft 1.21

- MC 1.21 support

## Version 1.4.7 for Minecraft 1.20.4

- Add japanese traduction
- Support for Forge 1.20.4

## Version 1.4.6 for Minecraft 1.19.4

- Add russian translation

## Version 1.4.6 for Minecraft 1.19.4

- Updated mod for forge 1.19.4
- Fixed text not displaying at the right place in GUI

## Version 1.4.5 for Minecraft 1.19.2

- Owner is not shown when the user is not yet resolved
- Support for Forge 1.19.2

## Version 1.4.4 for Minecraft 1.19.x

- Added owner (beta)
- Improved German translation (thanks @Greg-21)
- Updated mod to 1.19

## Version 1.4.3 for Minecraft 1.18.x

- Fixed issues related to GUI scale (anything other than guiScale=2) - thanks @Braboware

## Version 1.4.2 for Minecraft 1.18.x

- Updated mod to 1.18

## Version 1.4.2 for Minecraft 1.17.x

- Improved German translation (thanks @Greg-21)
- Updated mod to 1.17

## Version 1.4.2 for Minecraft 1.16.4 and 1.16.5

- Added German translation (thanks @manuelgrabowski)

## Version 1.4.1 for Minecaft 1.16.4 and 1.16.5

- Added Polish translation (thanks @Greg-21)

## Version 1.4.0 for Minecaft 1.16.4 and 1.16.5

- Added support for localization (only en_US and fr_FR are supported at the moment)

## Version 1.3.4 for Minecaft 1.16.4

*It is a fix for the 1.16.4 version of Minecraft. For 1.16.3 and less, use the 1.3.3 version if this one doesn't work*
- Fixed a crash with the 35.1.0-MC1.16.4 Forge version (something was wrong with mappings, forge may have made some breaking changes)

## Version 1.3.3 for Minecaft 1.16.x

*Note: you won't be able to see the statistics in the horse's GUI
with Realistic Horse Genetics loaded. I sent a request for them to
improve their compatibility ([see that message]()https://github.com/sekelsta/horse-colors/issues/11).
However, there is an overlay message showing the stats even if that mod is loaded :)*

- Fixed server compatibility issue
- Stats are now displayed instead of the "press {key} to dismount" message

## Version 1.3.2 for Minecaft 1.16.x

- Fixed a crash happening when the player opens the horse screen without being riding a horse - Thanks @ManDeJan

## Version 1.3.1 for Minecraft 1.16.x

*Built with forge 1.16.4 35.0.4, compatible with 1.16.1 - 1.16.4*
- Used an access modifier to get the Horse you're interacting with. It should fix all the issues that you had.

## Version 1.3.0 for Minecraft 1.16.x

- Added "displayMinMax" option. When turned off, it won't display the min and max statistics
    - Set to false by default
- Added a "slots" statistics for llamas

## Version 1.2.1 for Minecraft 1.16.x

- Fixed issue where right clicking a non tamed horse would show the stats of the latest ridden horse, and not the targeted one

## Version 1.2.0 for Minecraft 1.16.x

- Fixed the stats not showing when crouching and right clicking a horse
- It is now possible to get a horse's stats by crouching and right clicking it. **It only works if the horse is not tamed yet**

## Version 1.1.0 for Minecraft 1.16.x

*The config GUI has not been implemented by the Forge Team yet. To update the mod, you need to edit the config file in config/ folder of your minecraft directory*

- Now works with all 1.16 versions (it works with any other mod as well)
- Added a config file:
    - You can disable the stats from showing on the GUI. In that case, it will show the horse's stats when hovering its name (default: disabled)
    - You can disable the stats from showing with colors. In that case, it will use the default color (gray) (default: enabled)

## Version 1.0.0 for Minecraft 1.16.3

*Needs forge >= 34.0.6 (MC1.16.3)*
