## Version 3.5.2 Fabric

_supports Minecraft 26.2.x_

Requirements:
- Fabric API
- Cloth Config API `>=26.2.155`

Optional:
- Mod Menu, to edit the configuration from the Mods screen

Changes:
- Hardened interactions, HUD rendering, inventory rendering, and owner lookups so a feature failure no longer crashes the client.
- Sanitized invalid horse attributes, including `NaN` and infinite values.
- Replaced fragile click and HUD injections with Fabric events and a dedicated HUD layer.
- "Press Shift to dismount" hint stays removed when mounting a horse, but it does not hide unrelated vanilla messages.
- Fixed `coloredStats` being ignored in some displays and removed the duplicated owner in combined mode.
- Fixed the German translation resource filename.
- Limited compatibility to Minecraft 26.2.x; later Minecraft versions require a tested port.
