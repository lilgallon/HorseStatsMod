package dev.nero.horsestatsmod.config;

import dev.nero.horsestatsmod.HorseStatsMod;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public final ForgeConfigSpec.BooleanValue displayStats;
    public final ForgeConfigSpec.BooleanValue coloredStats;
    public final ForgeConfigSpec.BooleanValue displayMinMax;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("HorseStatsMod");

        displayStats = builder
                .comment("Shows the stats in the GUI. If turned off, you need to put your mouse on the horse name " +
                         "(in the GUI) to show a tooltip with its stats. Can be useful with some resource packs or " +
                         "mods that change the GUI of horses")
                .translation(HorseStatsMod.MODID + ".config." + "displayStats")
                .define("displayStats", false);

        coloredStats = builder
                .comment("Shows the stats with colors (only when displayStats is true). If turned on, it will " +
                        "display the stats with the default gray color. Can be useful with some resource packs.")
                .translation(HorseStatsMod.MODID + ".config." + "coloredStats")
                .define("coloredStats", true);

        displayMinMax = builder
                .comment("Shows the stats with their min and max. If turned off, it will display the stats without " +
                        "any information about their min and max.")
                .translation(HorseStatsMod.MODID + ".config." + "displayMinMax")
                .define("displayMinMax", true);

        builder.pop();
    }
}
