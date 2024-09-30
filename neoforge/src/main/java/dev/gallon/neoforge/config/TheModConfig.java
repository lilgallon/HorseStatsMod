package dev.gallon.neoforge.config;

import dev.gallon.domain.ModConfig;
import dev.gallon.domain.ModMetadata;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = ModMetadata.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class TheModConfig {

    public static final ClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfig config = new ModConfig();

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final net.neoforged.fml.event.config.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == TheModConfig.CLIENT_SPEC) {
            TheModConfig.bakeConfig();
        }
    }

    public static void bakeConfig() {
        config.setDisplayStats(CLIENT.displayStats.get());
        config.setColoredStats(CLIENT.coloredStats.get());
        config.setDisplayMinMax(CLIENT.displayMinMax.get());
    }
}
