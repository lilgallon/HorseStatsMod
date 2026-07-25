## Version 3.5.2 NeoForge

_supports Minecraft 26.2.x_

Changes:
- Hardened interactions, HUD rendering, inventory rendering, and owner lookups so a feature failure no longer crashes the client.
- Sanitized invalid horse attributes, including `NaN` and infinite values.
- Replaced fragile click and HUD injections with NeoForge events and a dedicated HUD layer.
- Restored suppression of the vanilla "Press Shift to dismount" hint when mounting a horse, without hiding unrelated vanilla messages.
- Fixed `coloredStats` being ignored in some displays and removed the duplicated owner in combined mode.
- Fixed the German translation resource filename.
- Removed the invalid update manifest URL.
- Limited compatibility to Minecraft 26.2.x; later Minecraft versions require a tested port.
