package dev.gallon.forge.config;

import dev.gallon.domain.ModConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public final class TheModConfig {
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ModConfig config = new ModConfig();

    static {
        Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }

    private TheModConfig() {
    }

    public static void register(BusGroup modBusGroup) {
        ModConfigEvent.Loading.getBus(modBusGroup).addListener(TheModConfig::onModConfigEvent);
        ModConfigEvent.Reloading.getBus(modBusGroup).addListener(TheModConfig::onModConfigEvent);
    }

    private static void onModConfigEvent(ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == CLIENT_SPEC) {
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        config.setDisplayStatsInInventory(CLIENT.displayStatsInInventory.get());
        config.setDisplayStatsOnInteraction(CLIENT.displayStatsOnInteraction.get());
        config.setColoredStats(CLIENT.coloredStats.get());
        config.setDisplayMinMax(CLIENT.displayMinMax.get());
        config.setDisplayStatsInPercentage(CLIENT.statsInPercentage.get());
        config.setGroupedStats(CLIENT.groupedStats.get());
        config.setIncludeAttributeModifiers(CLIENT.includeAttributeModifiers.get());
        config.resetInvalidValues();
    }

    public static void applyAndSave(ModConfig source) {
        CLIENT.displayStatsInInventory.set(source.getDisplayStatsInInventory());
        CLIENT.displayStatsOnInteraction.set(source.getDisplayStatsOnInteraction());
        CLIENT.coloredStats.set(source.getColoredStats());
        CLIENT.displayMinMax.set(source.getDisplayMinMax());
        CLIENT.statsInPercentage.set(source.getDisplayStatsInPercentage());
        CLIENT.groupedStats.set(source.getGroupedStats());
        CLIENT.includeAttributeModifiers.set(source.getIncludeAttributeModifiers());
        CLIENT_SPEC.save();
        bakeConfig();
    }
}
