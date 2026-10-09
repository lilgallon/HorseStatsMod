## Version 3.6.0 Forge

_supports Minecraft 26.3.x_

Tested loader:
- Forge `66.0.9` (client and dedicated server).

Changes:
- Ported the Forge build to Minecraft 26.3. Minecraft 26.2 is no longer supported by this version.
- New option `displayStatsAboveHead` to display the statistics above the mounts' heads, like a name tag:
  - `WHEN_LOOKING` (default): only above the mount you are looking at, up to 64 blocks away.
  - `ALWAYS`: above every mount within 32 blocks.
  - `DISABLED`: never.
