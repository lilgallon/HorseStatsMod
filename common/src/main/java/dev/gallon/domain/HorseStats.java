package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record HorseStats(
        @NotNull String name,
        @NotNull Double health,
        @NotNull Double jumpHeight,
        @NotNull Double speed,
        @NotNull Optional<Integer> slots,
        @NotNull Optional<String> owner,
        @NotNull MountType mountType
) {
    public @NotNull Integer minHealth() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Llama: 15 - 30
            case LLAMA -> 15;

            // https://minecraft.wiki/w/Trader_Llama: 15 - 30
            case TRADER_LLAMA -> 15;

            // https://minecraft.wiki/w/Donkey#Health: Donkey's health ranges from 15–30
            case DONKEY -> 15;

            // https://minecraft.wiki/w/Mule#Health: A mule's health ranges from 15–30, but tends toward the average of
            // 22–23. Displayed hearts are health, divided by two, rounded down
            case MULE -> 15;

            // https://minecraft.wiki/w/Horse#Health: Horse's health points range from 15HPx7.5 to 30HPx15
            case HORSE -> 15;

            // https://minecraft.wiki/w/Skeleton_Horse#Health: A skeleton horse's health is always 15. Displayed hearts
            // are health, divided by two, rounded down. A horse with an odd number of health points (15, 17, 19, etc.)
            // does not show the last half-heart. If the horse has lost one health point lower than the inflicted damage
            // and did not regenerate, it has an odd number of health points, otherwise, it has an even number of health
            // points.
            case SKELETON_HORSE -> 15;

            // https://minecraft.wiki/w/Zombie_Horse#Health: Zombie horses are spawned with a constant health of 25
            case ZOMBIE_HORSE -> 25;

            // https://minecraft.wiki/w/Camel
            case CAMEL -> 32;
        };
    }

    public @NotNull Integer maxHealth() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Llama: 15 - 30
            case LLAMA -> 30;

            // https://minecraft.wiki/w/Trader_Llama: 15 - 30
            case TRADER_LLAMA -> 30;

            // https://minecraft.wiki/w/Donkey#Health: Donkey's health ranges from 15–30
            case DONKEY -> 30;

            // https://minecraft.wiki/w/Mule#Health: A mule's health ranges from 15–30, but tends toward the average of
            // 22–23. Displayed hearts are health, divided by two, rounded down
            case MULE -> 30;

            // https://minecraft.wiki/w/Horse#Health: Horse's health points range from 15HPx7.5 to 30HPx15
            case HORSE -> 30;

            // https://minecraft.wiki/w/Skeleton_Horse#Health: A skeleton horse's health is always 15. Displayed hearts
            // are health, divided by two, rounded down. A horse with an odd number of health points (15, 17, 19, etc.)
            // does not show the last half-heart. If the horse has lost one health point lower than the inflicted damage
            // and did not regenerate, it has an odd number of health points, otherwise, it has an even number of health
            // points.
            case SKELETON_HORSE -> 15;

            // https://minecraft.wiki/w/Zombie_Horse#Health: Zombie horses are spawned with a constant health of 25
            case ZOMBIE_HORSE -> 25;

            // https://minecraft.wiki/w/Camel
            case CAMEL -> 32;
        };
    }

    public @NotNull Double minJumpHeight() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Jump_Strength_2: In Java Edition, A bred donkey's jump strength has the
            // same limits as other horses, between 0.4 and 1.0.
            case DONKEY -> 1.153;

            // https://minecraft.wiki/w/Mule#Jump_strength: Spawned mules' jump strength is usually 0.5, which is enough
            // to clear 1 9⁄16 blocks. Jump strengths between 0.4 and 1.0 can be found in bred mules, depending on the
            // statistics of the parents => same as horse
            case MULE -> 1.153;

            // https://minecraft.wiki/w/Horse#Jump_strength: Horse's jump strength ranges from 0.4–1.0, with an average
            // of 0.7 in internal units. The minimum jump strength of 0.4 is enough to clear 1.153 blocks, while the
            // maximum of 1.0 is enough to clear 5.9197 blocks. The calculation, however, is not linear with the average
            // jump strength of 0.7 favoring the lower side with the value of 3.124 blocks.
            case HORSE -> 1.153;

            // https://minecraft.wiki/w/Zombie_Horse#Jump_strength: Zombie horses' jump strength ranges from 0.5 to 0.7,
            // with an average of 0.6 in internal units. The minimum jump strength of 0.5 is enough to clear 1.7088
            // blocks, while the maximum of 0.7 is enough to clear 3.124 blocks. The average jump strength of 0.6 is
            // enough to clear 2.3675 blocks.
            case ZOMBIE_HORSE -> 1.7088;

            // https://minecraft.wiki/w/Skeleton_Horse#Jump_strength: Jump strength ranges from 0.4–1.0, averaging 0.7.
            // A jump strength of 0.5 is enough to clear 1 19/32 blocks, while 1.0 is enough to clear 5 9/32 blocks.
            case SKELETON_HORSE -> 1.59375;

            // https://minecraft.wiki/w/Llama: No mention of jump height
            case LLAMA -> 0.0;

            // https://minecraft.wiki/w/Trader_Llama: No mention of jump height
            case TRADER_LLAMA -> 0.0;

            // https://minecraft.wiki/w/Camel: Camels have the unique ability to step up 1.5 blocks, allowing them to
            // surmount fences and walls with ease. This is in contrast to horses, donkeys and mules, which can't
            // automatically step up more than one block without jumping. => no jump
            case CAMEL -> 0.0;
        };
    }

    public @NotNull Double maxJumpHeight() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Jump_Strength_2: In Java Edition, A bred donkey's jump strength has the
            // same limits as other horses, between 0.4 and 1.0.
            case DONKEY -> 5.9197;

            // https://minecraft.wiki/w/Mule#Jump_strength: Spawned mules' jump strength is usually 0.5, which is enough
            // to clear 1 9⁄16 blocks. Jump strengths between 0.4 and 1.0 can be found in bred mules, depending on the
            // statistics of the parents => same as horse
            case MULE -> 5.9197;

            // https://minecraft.wiki/w/Horse#Jump_strength: Horse's jump strength ranges from 0.4–1.0, with an average
            // of 0.7 in internal units. The minimum jump strength of 0.4 is enough to clear 1.153 blocks, while the
            // maximum of 1.0 is enough to clear 5.9197 blocks. The calculation, however, is not linear with the average
            // jump strength of 0.7 favoring the lower side with the value of 3.124 blocks.
            case HORSE -> 5.9197;

            // https://minecraft.wiki/w/Zombie_Horse#Jump_strength: Zombie horses' jump strength ranges from 0.5 to 0.7,
            // with an average of 0.6 in internal units. The minimum jump strength of 0.5 is enough to clear 1.7088
            // blocks, while the maximum of 0.7 is enough to clear 3.124 blocks. The average jump strength of 0.6 is
            // enough to clear 2.3675 blocks.
            case ZOMBIE_HORSE -> 3.124;

            // https://minecraft.wiki/w/Skeleton_Horse#Jump_strength: Jump strength ranges from 0.4–1.0, averaging 0.7.
            // A jump strength of 0.5 is enough to clear 1 19/32 blocks, while 1.0 is enough to clear 5 9/32 blocks.
            case SKELETON_HORSE -> 1.59375;

            // https://minecraft.wiki/w/Llama: No mention of jump height
            case LLAMA -> 0.0;

            // https://minecraft.wiki/w/Trader_Llama: No mention of jump height
            case TRADER_LLAMA -> 0.0;

            // https://minecraft.wiki/w/Camel: Camels have the unique ability to step up 1.5 blocks, allowing them to
            // surmount fences and walls with ease. This is in contrast to horses, donkeys and mules, which can't
            // automatically step up more than one block without jumping. => no jump
            case CAMEL -> 0.0;
        };
    }

    public @NotNull Double minSpeed() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Movement_speed: A spawned donkey's speed value is always 0.175, but it
            // moves at the slightly reduced 7.38 blocks/sec. For reference, the player's normal walking speed is 0.1,
            // equivalent to 4.3 blocks/sec, and the player's sprinting speed is equivalent to 5.6 blocks/sec. The speed
            // listed does not include any status effect that affects the speed of a horse or a player.
            // But, in https://minecraft.wiki/w/Donkey#Movement_Speed_2: In Java Edition, a bred donkey's speed value
            // has the same limits as other horses, between 0.1125 and 0.3375.
            case DONKEY -> 4.856625;

            // https://minecraft.wiki/w/Mule#Movement_speed: Spawned mules' speed is always 0.175; the player's normal
            // walking speed is 0.1. The speed listed does not include any status effect that affects the speed of a
            // horse or a player. Bred mules have speed between 0.1125 and 0.3375 based on their parent's speeds, like
            // all other horse breeding. => same as horse
            case MULE -> 4.856625;

            // https://minecraft.wiki/w/Horse#Movement_speed: Horse's movement speed ranges from 0.1125–0.3375 in
            // internal units, with an average of 0.225. For reference, the player's normal walking speed is 0.1. The
            // speed listed does not include any status effect that affects the speed of the horse or the player.
            // The conversion factor between internal units and blocks/sec is roughly 43.17, putting the best horse's
            // maximum speed at about 14.57 blocks/second, and the average horse's speed at about 9.71 blocks/sec
            // => 0.1125*43.17
            case HORSE -> 4.856625;

            // https://minecraft.wiki/w/Skeleton_Horse#Movement_speed: Skeleton horse's speed is always 0.2; the
            // player's normal walking speed is 0.1. The speed listed does not include any status effect that affects
            // the speed of a horse or a player. => 2 times the player's normal walking speed (4.317*2)
            case SKELETON_HORSE -> 8.634;

            // https://minecraft.wiki/w/Zombie_Horse#Movement_speed: In Java Edition, it ranges between ~0.21347 and
            // ~0.28463 in internal units, with an average of ~0.24905. The conversion factor between internal units and
            // blocks/sec is roughly 43.17
            case ZOMBIE_HORSE -> 9.216;

            // https://minecraft.wiki/w/Llama: 0.175 internal units
            case LLAMA -> 7.55475;

            // https://minecraft.wiki/w/Trader_Llama: 0.175 internal units
            case TRADER_LLAMA -> 7.55475;

            // https://minecraft.wiki/w/Camel: Camels are less maneuverable than other mounts, with their turning and
            // strafing speed being slower. They gradually gain speed while moving in a straight line, up to a walking
            // speed of 3.885 blocks/s, and can also manually sprint at a speed of 8.203 blocks/s by double tapping
            // forward or by holding the sprint control while moving forward.
            case CAMEL -> 8.203;
        };
    }

    public @NotNull Double maxSpeed() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Movement_speed: A spawned donkey's speed value is always 0.175, but it
            // moves at the slightly reduced 7.38 blocks/sec. For reference, the player's normal walking speed is 0.1,
            // equivalent to 4.3 blocks/sec, and the player's sprinting speed is equivalent to 5.6 blocks/sec. The speed
            // listed does not include any status effect that affects the speed of a horse or a player.
            // But, in https://minecraft.wiki/w/Donkey#Movement_Speed_2: In Java Edition, a bred donkey's speed value
            // has the same limits as other horses, between 0.1125 and 0.3375.
            case DONKEY -> 14.569875;

            // https://minecraft.wiki/w/Mule#Movement_speed: Spawned mules' speed is always 0.175; the player's normal
            // walking speed is 0.1. The speed listed does not include any status effect that affects the speed of a
            // horse or a player. Bred mules have speed between 0.1125 and 0.3375 based on their parent's speeds, like
            // all other horse breeding. => same as horse
            case MULE -> 14.569875;

            // https://minecraft.wiki/w/Horse#Movement_speed: Horse's movement speed ranges from 0.1125–0.3375 in
            // internal units, with an average of 0.225. For reference, the player's normal walking speed is 0.1. The
            // speed listed does not include any status effect that affects the speed of the horse or the player.
            // The conversion factor between internal units and blocks/sec is roughly 43.17, putting the best horse's
            // maximum speed at about 14.57 blocks/second, and the average horse's speed at about 9.71 blocks/sec
            // => 0.1125*43.17
            case HORSE -> 14.569875;

            // https://minecraft.wiki/w/Skeleton_Horse#Movement_speed: Skeleton horse's speed is always 0.2; the
            // player's normal walking speed is 0.1. The speed listed does not include any status effect that affects
            // the speed of a horse or a player. => 2 times the player's normal walking speed (4.317*2)
            case SKELETON_HORSE -> 8.634;

            // https://minecraft.wiki/w/Zombie_Horse#Movement_speed: In Java Edition, it ranges between ~0.2134746, and
            // ~0.28463 in internal units, with an average of ~0.24905. The conversion factor between internal units and
            // blocks/sec is roughly 43.17
            case ZOMBIE_HORSE -> 12.288;

            // https://minecraft.wiki/w/Llama: 0.175 internal units
            case LLAMA -> 7.55475;

            // https://minecraft.wiki/w/Trader_Llama: 0.175 internal units
            case TRADER_LLAMA -> 7.55475;

            // https://minecraft.wiki/w/Camel: Camels are less maneuverable than other mounts, with their turning and
            // strafing speed being slower. They gradually gain speed while moving in a straight line, up to a walking
            // speed of 3.885 blocks/s, and can also manually sprint at a speed of 8.203 blocks/s by double tapping
            // forward or by holding the sprint control while moving forward.
            case CAMEL -> 8.203;
        };
    }

    public @NotNull Integer minSlots() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Equipment: An additional 15 inventory slots, provided the donkey has been
            // equipped with a chest.
            case DONKEY -> 15;

            // https://minecraft.wiki/w/Mule#Equipment: A mule starts out with just a saddle slot, but if it is given a
            // chest, it acquires 15 more inventory slots that can hold any items.
            case MULE -> 15;

            // https://minecraft.wiki/w/Llama#Storage: A tamed llama can be equipped with a chest by pressing the use
            // control on it while holding a chest. The chest gives the llama 3 to 15 slots of inventory space,
            // depending on its strength
            case LLAMA -> 3;

            // https://minecraft.wiki/w/Trader_Llama#Storage: A tamed trader llama can be equipped with a chest by
            // pressing the use control on it while holding a chest. The chest gives the trader llama 3 to 15 slots of
            // inventory space, depending on its strength
            case TRADER_LLAMA -> 3;

            // No mention of slots in wiki
            case ZOMBIE_HORSE, SKELETON_HORSE, HORSE, CAMEL -> 0;
        };
    }

    public @NotNull Integer maxSlots() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Equipment: An additional 15 inventory slots, provided the donkey has been
            // equipped with a chest.
            case DONKEY, MULE -> 15;

            // https://minecraft.wiki/w/Llama#Storage: A tamed llama can be equipped with a chest by pressing the use
            // control on it while holding a chest. The chest gives the llama 3 to 15 slots of inventory space,
            // depending on its strength
            case LLAMA -> 15;

            // https://minecraft.wiki/w/Trader_Llama#Storage: A tamed trader llama can be equipped with a chest by
            // pressing the use control on it while holding a chest. The chest gives the trader llama 3 to 15 slots of
            // inventory space, depending on its strength
            case TRADER_LLAMA -> 15;

            // No mention of slots in wiki
            case ZOMBIE_HORSE, SKELETON_HORSE, HORSE, CAMEL -> 0;
        };
    }

    private @NotNull Integer computePercentage(double value, double min, double max) {
        if (max == min) {
            return max == 0 ? 0 : 100;
        } else {
            return (int) ((value - min) / (max - min) * 100);
        }
    }

    private @NotNull Integer getHealthPercentage() {
        return computePercentage(health, minHealth(), maxHealth());
    }

    private @NotNull Integer getJumpHeightPercentage() {
        return computePercentage(jumpHeight, minJumpHeight(), maxJumpHeight());
    }

    private @NotNull Integer getSpeedPercentage() {
        return computePercentage(speed, minSpeed(), maxSpeed());
    }

    private @NotNull Integer getSlotsPercentage() {
        return computePercentage(slots.orElse(minSlots()), minSlots(), maxSlots());
    }

    public @NotNull String getHealthStr(Boolean percentage) {
        return percentage ? (getHealthPercentage() + "%") : String.format("%.2f", health);
    }

    public @NotNull String getJumpHeightStr(Boolean percentage) {
        return percentage ? (getJumpHeightPercentage() + "%") : String.format("%.2f", jumpHeight);
    }

    public @NotNull String getSpeedStr(Boolean percentage) {
        return percentage ? (getSpeedPercentage() + "%") : String.format("%.2f", speed);
    }

    public @NotNull String getSlotsStr(Boolean percentage) {
        return percentage ? (getSlotsPercentage() + "%") : slots.orElse(0).toString();
    }

    public @NotNull Integer getGroupedStats() {
        return computePercentage(
                health + speed + jumpHeight,
                minHealth() + minSpeed() + minJumpHeight(),
                maxHealth() + maxSpeed() + maxJumpHeight()
        );
    }

    public @NotNull String getGroupedStatsStr() {
        return getGroupedStats() + "%";
    }
}
