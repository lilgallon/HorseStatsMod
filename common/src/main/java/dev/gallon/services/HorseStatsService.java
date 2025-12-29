package dev.gallon.services;

import dev.gallon.domain.HorseStats;
import dev.gallon.domain.MountType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.*;
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
                            JumpHeightConverter.getJumpHeight(jump),
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

    static public Double convertSpeedToBlocksPerSeconds(Double speed) {
        // https://minecraft.wiki/w/Horse#Movement_speed: The conversion factor between internal units and blocks/sec is
        // roughly 43.17, putting the best horse's maximum speed at about 14.57 blocks/second, and the average horse's
        // speed at about 9.71 blocks/sec.
        // PERSONAL NOTE: This can change according to the TPS (ticks per seconds). But since all min / max are computed
        // according to this 43.17 value, we keep it to stay coherent
        return speed * 43.17;
    }
}
