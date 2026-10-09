## Version 3.6.0 Fabric

_supports Minecraft 26.3.x_

Tested loader:
- Fabric Loader `0.19.5` (client and dedicated server).

Requirements:
- Fabric Loader `>=0.19.5`
- Fabric API `>=0.162.0+26.3`
- Cloth Config API `>=26.3.159`

Optional:
- Mod Menu `21.0.0`, to edit the configuration from the Mods screen

Changes:
- New option `displayStatsAboveHead` to display the statistics above the mounts' heads, like a name tag:
  - `WHEN_LOOKING`: only above the mount you are looking at, up to 64 blocks away.
  - `ALWAYS`: above every mount within 32 blocks.
  - `DISABLED` (default): never.
- Fixed the configuration screen crashing the game when saving, which prevented any setting from being saved.
- Updated Fabric API, Cloth Config API, and Mod Menu.
