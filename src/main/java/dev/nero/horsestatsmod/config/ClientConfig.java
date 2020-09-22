package dev.nero.horsestatsmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public final ForgeConfigSpec.BooleanValue displayStats;
    public final ForgeConfigSpec.BooleanValue coloredStats;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("HorseStatsMod");

        displayStats = builder
                .comment("Shows the stats in the GUI. If turned off, you need to put your mouse on the horse name " +
                         "(in the GUI) to show a tooltip with its stats. Can be useful with some resource packs or " +
                         "mods that change the GUI of horses")
                .translation("horsestatsmod" + ".config." + "displayStats")
                .define("displayStats", true);

        coloredStats = builder
                .comment("Shows the stats with colors (ignored if displayStats is false). If turned on, it will " +
                         " display the stats with the default gray color. Can be useful with some resource packs.")
                .translation("horsestatsmod" + ".config." + "coloredStats")
                .define("coloredStats", true);

        builder.pop();
    }
}
