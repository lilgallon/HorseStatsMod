package dev.gallon.fabric.config;

import dev.gallon.domain.ModConfig;
import dev.gallon.domain.ModMetadata;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ModMetadata.MOD_ID)
public class TheModConfig implements ConfigData {
    // documentation: https://shedaniel.gitbook.io/cloth-config/auto-config/creating-a-config-class

    @ConfigEntry.Gui.CollapsibleObject
    public final ModConfig modConfig = new ModConfig();
}
