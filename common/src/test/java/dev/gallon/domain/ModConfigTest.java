package dev.gallon.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModConfigTest {
    @Test
    void declaresNoStaticFieldSoConfigGuisDoNotTryToWriteIt() {
        for (Field field : ModConfig.class.getDeclaredFields()) {
            assertFalse(Modifier.isStatic(field.getModifiers()), field.getName() + " must not be static");
        }
    }

    @Test
    void resetsEveryNullConfigEntryToItsDefaultValue() throws IllegalAccessException {
        ModConfig config = new ModConfig();

        for (Field field : ModConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                field.setAccessible(true);
                field.set(config, null);
            }
        }

        config.resetInvalidValues();

        for (Field field : ModConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                field.setAccessible(true);
                assertNotNull(field.get(config), field.getName() + " was not reset");
            }
        }

        assertTrue(config.getDisplayStatsInInventory());
        assertEquals(InteractionKind.RIGHT_CLICK, config.getDisplayStatsOnInteraction());
        assertTrue(config.getColoredStats());
        assertEquals(DisplayMinMax.DISABLED, config.getDisplayMinMax());
        assertFalse(config.getDisplayStatsInPercentage());
        assertFalse(config.getPercentageWithDecimal());
        assertEquals(0, config.getHealthDecimals());
        assertEquals(2, config.getJumpHeightDecimals());
        assertEquals(2, config.getSpeedDecimals());
        assertEquals(GroupedKind.INDIVIDUAL, config.getGroupedStats());
        assertTrue(config.getIncludeAttributeModifiers());
        assertEquals(AboveHeadKind.DISABLED, config.getDisplayStatsAboveHead());
    }

    @Test
    void gettersRemainSafeBeforePostLoadValidationRuns() throws ReflectiveOperationException {
        ModConfig config = new ModConfig();
        Field groupedStats = ModConfig.class.getDeclaredField("groupedStats");
        groupedStats.setAccessible(true);
        groupedStats.set(config, null);

        assertEquals(GroupedKind.INDIVIDUAL, config.getGroupedStats());
    }

    @Test
    void keepsTheNumberOfDecimalsBetweenZeroAndFive() throws ReflectiveOperationException {
        ModConfig config = new ModConfig();

        config.setHealthDecimals(-1);
        config.setJumpHeightDecimals(6);
        config.setSpeedDecimals(3);

        assertEquals(0, config.getHealthDecimals());
        assertEquals(5, config.getJumpHeightDecimals());
        assertEquals(3, config.getSpeedDecimals());

        // A manually edited config file bypasses the setters
        Field speedDecimals = ModConfig.class.getDeclaredField("speedDecimals");
        speedDecimals.setAccessible(true);
        speedDecimals.set(config, 42);

        assertEquals(5, config.getSpeedDecimals());
    }
}
