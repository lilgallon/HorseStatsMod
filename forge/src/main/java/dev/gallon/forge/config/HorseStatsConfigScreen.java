package dev.gallon.forge.config;

import com.mojang.logging.LogUtils;
import dev.gallon.domain.AboveHeadKind;
import dev.gallon.domain.DisplayMinMax;
import dev.gallon.domain.GroupedKind;
import dev.gallon.domain.I18nKeys;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.ModConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

public final class HorseStatsConfigScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int OPTION_WIDTH = 280;
    private static final int BUTTON_WIDTH = 95;
    private static final int BUTTON_HEIGHT = 20;
    private static final String OPTION_PREFIX = "text.autoconfig.horsestatsmod.option.modConfig.";

    private final Screen parent;
    private final ModConfig draft;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public HorseStatsConfigScreen(Screen parent) {
        this(parent, copy(TheModConfig.config));
    }

    private HorseStatsConfigScreen(Screen parent, ModConfig draft) {
        super(Component.translatable("text.autoconfig.horsestatsmod.title"));
        this.parent = parent;
        this.draft = draft;
    }

    @Override
    protected void init() {
        layout.removeChildren();
        layout.addTitleHeader(title, font);

        LinearLayout options = layout.addToContents(LinearLayout.vertical().spacing(4));
        options.addChild(booleanOption(
                I18nKeys.DISPLAY_STATS_IN_INVENTORY,
                draft.getDisplayStatsInInventory(),
                draft::setDisplayStatsInInventory
        ));
        options.addChild(enumOption(
                I18nKeys.DISPLAY_STATS_ON_INTERACTION,
                draft.getDisplayStatsOnInteraction(),
                InteractionKind.values(),
                value -> enumLabel("displayStatsOnInteraction", value, interactionFallback(value)),
                draft::setDisplayStatsOnInteraction
        ));
        options.addChild(booleanOption(I18nKeys.COLORED_STATS, draft.getColoredStats(), draft::setColoredStats));
        options.addChild(enumOption(
                I18nKeys.DISPLAY_MIN_MAX,
                draft.getDisplayMinMax(),
                DisplayMinMax.values(),
                value -> enumLabel("displayMinMax", value, displayMinMaxFallback(value)),
                draft::setDisplayMinMax
        ));
        options.addChild(booleanOption(
                I18nKeys.STATS_IN_PERCENTAGE,
                draft.getDisplayStatsInPercentage(),
                draft::setDisplayStatsInPercentage
        ));
        options.addChild(enumOption(
                I18nKeys.GROUPED_STATS,
                draft.getGroupedStats(),
                GroupedKind.values(),
                value -> enumLabel("groupedStats", value, groupedFallback(value)),
                draft::setGroupedStats
        ));
        options.addChild(booleanOption(
                I18nKeys.INCLUDE_ATTRIBUTE_MODIFIERS,
                draft.getIncludeAttributeModifiers(),
                draft::setIncludeAttributeModifiers
        ));
        options.addChild(enumOption(
                I18nKeys.DISPLAY_STATS_ABOVE_HEAD,
                draft.getDisplayStatsAboveHead(),
                AboveHeadKind.values(),
                value -> enumLabel("displayStatsAboveHead", value, aboveHeadFallback(value)),
                draft::setDisplayStatsAboveHead
        ));

        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(
                Component.translatableWithFallback("horsestatsmod.configuration.reset", "Reset"),
                button -> minecraft.gui.setScreen(new HorseStatsConfigScreen(parent, new ModConfig()))
        ).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> saveAndClose())
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }

    private void saveAndClose() {
        try {
            TheModConfig.applyAndSave(draft);
            minecraft.gui.setScreen(parent);
        } catch (RuntimeException exception) {
            LOGGER.error("Unable to save HorseStatsMod Forge client configuration", exception);
        }
    }

    private static CycleButton<Boolean> booleanOption(String key, boolean initialValue, Consumer<Boolean> setter) {
        return CycleButton.onOffBuilder(initialValue).create(
                0,
                0,
                OPTION_WIDTH,
                BUTTON_HEIGHT,
                Component.translatable(key),
                (button, value) -> setter.accept(value)
        );
    }

    private static <T> CycleButton<T> enumOption(
            String key,
            T initialValue,
            T[] values,
            Function<T, Component> valueLabel,
            Consumer<T> setter
    ) {
        return CycleButton.builder(valueLabel, initialValue)
                .withValues(values)
                .create(
                        0,
                        0,
                        OPTION_WIDTH,
                        BUTTON_HEIGHT,
                        Component.translatable(key),
                        (button, value) -> setter.accept(value)
                );
    }

    private static Component enumLabel(String option, Enum<?> value, String fallback) {
        return Component.translatableWithFallback(OPTION_PREFIX + option + "." + value.name(), fallback);
    }

    private static String interactionFallback(InteractionKind value) {
        return switch (value) {
            case RIGHT_CLICK -> "Right click";
            case SHIFT_RIGHT_CLICK -> "Shift + right click";
            case RIGHT_OR_SHIFT_RIGHT_CLICK -> "Right click or Shift + right click";
            case MIDDLE_CLICK -> "Middle click";
            case DISABLED -> "Disabled";
        };
    }

    private static String displayMinMaxFallback(DisplayMinMax value) {
        return switch (value) {
            case MIN_AND_MAX -> "Minimum and maximum";
            case MIN_ONLY -> "Minimum only";
            case MAX_ONLY -> "Maximum only";
            case DISABLED -> "Disabled";
        };
    }

    private static String groupedFallback(GroupedKind value) {
        return switch (value) {
            case INDIVIDUAL -> "Individual";
            case GROUPED -> "Grouped";
            case GROUPED_AND_INDIVIDUAL -> "Grouped and individual";
        };
    }

    private static String aboveHeadFallback(AboveHeadKind value) {
        return switch (value) {
            case WHEN_LOOKING -> "When looking at it";
            case ALWAYS -> "Always";
            case DISABLED -> "Disabled";
        };
    }

    private static @NotNull ModConfig copy(ModConfig source) {
        ModConfig copy = new ModConfig();
        copy.setDisplayStatsInInventory(source.getDisplayStatsInInventory());
        copy.setDisplayStatsOnInteraction(source.getDisplayStatsOnInteraction());
        copy.setColoredStats(source.getColoredStats());
        copy.setDisplayMinMax(source.getDisplayMinMax());
        copy.setDisplayStatsInPercentage(source.getDisplayStatsInPercentage());
        copy.setGroupedStats(source.getGroupedStats());
        copy.setIncludeAttributeModifiers(source.getIncludeAttributeModifiers());
        copy.setDisplayStatsAboveHead(source.getDisplayStatsAboveHead());
        return copy;
    }
}
