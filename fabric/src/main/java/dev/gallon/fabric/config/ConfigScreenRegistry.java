package dev.gallon.fabric.config;

import dev.gallon.domain.AboveHeadKind;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.DisplayMinMax;
import dev.gallon.domain.GroupedKind;
import dev.gallon.domain.ModConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.List;

public final class ConfigScreenRegistry {
    private static final String INTERACTION_FIELD = "displayStatsOnInteraction";
    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

    private ConfigScreenRegistry() {
    }

    public static void register() {
        AutoConfigClient.getGuiRegistry(TheModConfig.class).registerPredicateProvider(
                ConfigScreenRegistry::createInteractionKindEntry,
                ConfigScreenRegistry::isInteractionKindField
        );
        AutoConfigClient.getGuiRegistry(TheModConfig.class).registerPredicateProvider(
                (key, field, config, defaults, registry) -> List.of(ENTRY_BUILDER.startSelector(
                                Component.translatable(key), DisplayMinMax.values(),
                                ((ModConfig) config).getDisplayMinMax())
                        .setNameProvider(value -> Component.translatable(key + "." + value.name()))
                        .setDefaultValue(((ModConfig) defaults)::getDisplayMinMax)
                        .setSaveConsumer(((ModConfig) config)::setDisplayMinMax)
                        .build()),
                field -> field.getDeclaringClass() == ModConfig.class
                        && field.getName().equals("displayMinMax") && field.getType() == DisplayMinMax.class
        );
        AutoConfigClient.getGuiRegistry(TheModConfig.class).registerPredicateProvider(
                (key, field, config, defaults, registry) -> List.of(ENTRY_BUILDER.startSelector(
                                Component.translatable(key), GroupedKind.values(),
                                ((ModConfig) config).getGroupedStats())
                        .setNameProvider(value -> Component.translatable(key + "." + value.name()))
                        .setDefaultValue(((ModConfig) defaults)::getGroupedStats)
                        .setSaveConsumer(((ModConfig) config)::setGroupedStats)
                        .build()),
                field -> field.getDeclaringClass() == ModConfig.class
                        && field.getName().equals("groupedStats") && field.getType() == GroupedKind.class
        );
        AutoConfigClient.getGuiRegistry(TheModConfig.class).registerPredicateProvider(
                (key, field, config, defaults, registry) -> List.of(ENTRY_BUILDER.startSelector(
                                Component.translatable(key), AboveHeadKind.values(),
                                ((ModConfig) config).getDisplayStatsAboveHead())
                        .setNameProvider(value -> Component.translatable(key + "." + value.name()))
                        .setDefaultValue(((ModConfig) defaults)::getDisplayStatsAboveHead)
                        .setSaveConsumer(((ModConfig) config)::setDisplayStatsAboveHead)
                        .build()),
                field -> field.getDeclaringClass() == ModConfig.class
                        && field.getName().equals("displayStatsAboveHead") && field.getType() == AboveHeadKind.class
        );
    }

    private static boolean isInteractionKindField(Field field) {
        return field.getDeclaringClass() == ModConfig.class
                && field.getName().equals(INTERACTION_FIELD)
                && field.getType() == InteractionKind.class;
    }

    private static List<AbstractConfigListEntry> createInteractionKindEntry(
            String translationKey,
            Field field,
            Object config,
            Object defaults,
            GuiRegistryAccess registryAccess
    ) {
        ModConfig currentConfig = (ModConfig) config;
        ModConfig defaultConfig = (ModConfig) defaults;

        var entry = ENTRY_BUILDER.startSelector(
                        Component.translatable(translationKey),
                        InteractionKind.values(),
                        currentConfig.getDisplayStatsOnInteraction()
                )
                .setNameProvider(ConfigScreenRegistry::getLabel)
                .setDefaultValue(defaultConfig::getDisplayStatsOnInteraction)
                .setSaveConsumer(currentConfig::setDisplayStatsOnInteraction)
                .build();

        return List.of(entry);
    }

    private static Component getLabel(InteractionKind value) {
        String key = "text.autoconfig.horsestatsmod.option.modConfig.displayStatsOnInteraction." + value.name();
        return Component.translatableWithFallback(key, getFallbackLabel(value));
    }

    private static String getFallbackLabel(InteractionKind value) {
        return switch (value) {
            case RIGHT_CLICK -> "Right click";
            case SHIFT_RIGHT_CLICK -> "Shift + right click";
            case RIGHT_OR_SHIFT_RIGHT_CLICK -> "Right click or Shift + right click";
            case MIDDLE_CLICK -> "Middle click";
            case DISABLED -> "Disabled";
        };
    }
}
