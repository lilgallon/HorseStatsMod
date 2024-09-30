package dev.gallon.services;

import dev.gallon.domain.HorseStats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

import static dev.gallon.services.UserCacheService.usernameCache;

public class HorseStatsService {
    static public Optional<HorseStats> getHorseStats(@NotNull AbstractHorse horse) {
        final Optional<AttributeInstance> healthAttr = Optional.ofNullable(horse.getAttribute(Attributes.MAX_HEALTH));
        final Optional<AttributeInstance> jumpAttr = Optional.ofNullable(horse.getAttribute(Attributes.JUMP_STRENGTH));
        final Optional<AttributeInstance> speedAttr = Optional.ofNullable(horse.getAttribute(Attributes.MOVEMENT_SPEED));

        if (healthAttr.isPresent() && jumpAttr.isPresent() && speedAttr.isPresent()) {
            final Optional<Component> name = Optional.ofNullable(horse.getDisplayName());
            final Double health = healthAttr.get().getValue();
            final Double jump = jumpAttr.get().getValue();
            final Double speed = speedAttr.get().getValue();
            final UUID ownerUUID = horse.getOwnerUUID();

            return Optional.of(
                    new HorseStats(
                            name.map(Component::getString),
                            health,
                            convertJumpToBlocks(jump),
                            convertSpeedToBlocksPerSeconds(speed),
                            Optional.ofNullable(horse instanceof Llama ? horse.getInventoryColumns() * 3 : null),
                            Optional.ofNullable(ownerUUID != null ? usernameCache.getUnchecked(ownerUUID).orElse(null) : null)
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    static public Double convertJumpToBlocks(Double jump) {
        return -0.1817584952 * Math.pow(jump, 3) +
                3.689713992 * Math.pow(jump, 2) +
                2.128599134 * jump - 0.343930367;
    }

    static public Double convertSpeedToBlocksPerSeconds(Double speed) {
        return speed * 42.16;
    }
}
