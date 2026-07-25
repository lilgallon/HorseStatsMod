package dev.gallon.services;

import net.minecraft.core.Holder;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class HorseStatsServiceTest {
    @Test
    void choosesBetweenBaseAndModifiedAttributeValues() {
        assertEquals(10, HorseStatsService.selectAttributeValue(10, 15, false));
        assertEquals(15, HorseStatsService.selectAttributeValue(10, 15, true));
    }

    @Test
    void sanitizesNonFiniteBaseAttributeValues() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        var attribute = new RangedAttribute("test.horse_stat", 5, 1, 10);
        var instance = new AttributeInstance(Holder.direct(attribute), ignored -> {
        });

        instance.setBaseValue(Double.NaN);
        assertEquals(1, HorseStatsService.selectAttributeValue(instance, false));

        instance.setBaseValue(Double.POSITIVE_INFINITY);
        assertEquals(10, HorseStatsService.selectAttributeValue(instance, false));

        instance.setBaseValue(Double.NEGATIVE_INFINITY);
        assertEquals(1, HorseStatsService.selectAttributeValue(instance, false));

        instance.setBaseValue(-10);
        assertEquals(1, HorseStatsService.selectAttributeValue(instance, false));

        instance.setBaseValue(20);
        assertEquals(10, HorseStatsService.selectAttributeValue(instance, false));
    }

    @Test
    void rejectsStatsWhenARequiredAttributeIsAbsent() {
        Optional<AttributeInstance> present = Optional.of(new AttributeInstance(
                Holder.direct(new RangedAttribute("test.horse_stat", 5, 1, 10)),
                ignored -> {
                }
        ));

        assertFalse(HorseStatsService.hasRequiredAttributes(present, Optional.empty(), present));
    }
}
