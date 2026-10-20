## Unreleased

Changes:
- Percentages are now rounded to the nearest value instead of being cut off: 65.6% is displayed as 66% instead of 65%.
  A mount with the minimum or maximum possible stat now shows exactly 0% or 100%.
- New option `percentageWithDecimal` to display the percentages with one decimal (65.6%).
- New options `healthDecimals`, `jumpHeightDecimals` and `speedDecimals` to choose how many decimals are displayed
  for each stat, from 0 to 5. Bred horses have a fractional health, set `healthDecimals` to see it.
- Fixed the jump height: it was up to almost one block too low (3.12 blocks were displayed as 2.84). It is now
  computed the same way the game moves a jumping horse.
- Fixed the minimum jump height of skeleton horses, which have the same 0.4 - 1.0 jump strength range as horses.
- The minimum and maximum speeds now match the game's exact values.
- Removed the player's walking speed, sprinting speed, jump height and health from the inventory tooltips.
- The stats in the horse inventory are now spaced according to their width, so that more decimals don't overlap.

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
