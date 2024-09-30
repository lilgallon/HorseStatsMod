package dev.gallon.neoforge.config;

import dev.gallon.domain.I18nKeys;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public final ModConfigSpec.BooleanValue displayStats;
    public final ModConfigSpec.BooleanValue coloredStats;
    public final ModConfigSpec.BooleanValue displayMinMax;

    public ClientConfig(ModConfigSpec.Builder builder) {
        builder.push("HorseStatsMod");

        displayStats = builder
                .comment("Shows the stats in the GUI. If turned off, you need to put your mouse on the horse name " +
                        "(in the GUI) to show a tooltip with its stats. Can be useful with some resource packs or " +
                        "mods that change the GUI of horses")
                .translation(I18nKeys.DISPLAY_STATS)
                .define("displayStats", false);

        coloredStats = builder
                .comment("Shows the stats with colors (only when displayStats is true). If turned on, it will " +
                        "display the stats with the default gray color. Can be useful with some resource packs.")
                .translation(I18nKeys.COLORED_STATS)
                .define("coloredStats", true);

        displayMinMax = builder
                .comment("Shows the stats with their min and max. If turned off, it will display the stats without " +
                        "any information about their min and max.")
                .translation(I18nKeys.DISPLAY_MIN_MAX)
                .define("displayMinMax", false);

        builder.pop();
    }
}
