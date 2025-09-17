package dev.gallon.services;

import dev.gallon.domain.HorseStats;
import dev.gallon.domain.MountType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.*;
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
            final Component name = horse.getDisplayName();
            final Double health = healthAttr.get().getValue();
            final Double jump = jumpAttr.get().getValue();
            final Double speed = speedAttr.get().getValue();
            final Optional<UUID> ownerUUID = Optional.ofNullable(horse.getOwner()).map(LivingEntity::getUUID);
            final int slots = horse.getInventoryColumns() * 3;

            return Optional.of(
                    new HorseStats(
                            name.getString(),
                            health,
                            convertJumpToBlocks(jump),
                            convertSpeedToBlocksPerSeconds(speed),
                            Optional.ofNullable(slots == 0 ? null : slots),
                            ownerUUID.flatMap(usernameCache::getUnchecked),
                            switch (horse) {
                                case Horse ignored -> MountType.HORSE;
                                case Camel ignored -> MountType.CAMEL;
                                case ZombieHorse ignored -> MountType.ZOMBIE_HORSE;
                                case Mule ignored -> MountType.MULE;
                                case TraderLlama ignored -> MountType.TRADER_LLAMA;
                                case Llama ignored -> MountType.LLAMA;
                                case Donkey ignored -> MountType.DONKEY;
                                case SkeletonHorse ignored -> MountType.SKELETON_HORSE;
                                default -> MountType.HORSE;
                            }
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    static public Double convertJumpToBlocks(Double jump) {
        Double convertedJump = 0.0;
        while (jump > 0) {
            convertedJump += jump;
            jump = (jump - .08) * .98 * .98;
        }
        return convertedJump;
    }

    static public Double convertSpeedToBlocksPerSeconds(Double speed) {
        return speed * 42.157796;
    }
}
