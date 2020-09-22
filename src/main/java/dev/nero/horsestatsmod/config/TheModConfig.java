package dev.nero.horsestatsmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = "horsestatsmod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TheModConfig {

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    private static boolean displayStats;
    private static boolean coloredStats;

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == TheModConfig.CLIENT_SPEC) {
            TheModConfig.bakeConfig();
        }
    }

    public static void bakeConfig() {
        displayStats = CLIENT.displayStats.get();
        coloredStats = CLIENT.coloredStats.get();
    }

    public static boolean displayStats() {
        return displayStats;
    }

    public static void setDisplayStats(boolean displayStats) {
        TheModConfig.displayStats = displayStats;
    }

    public static boolean coloredStats() {
        return coloredStats;
    }

    public static void setColoredStats(boolean coloredStats) {
        TheModConfig.coloredStats = coloredStats;
    }
}
