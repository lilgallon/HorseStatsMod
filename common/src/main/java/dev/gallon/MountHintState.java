package dev.gallon;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

final class MountHintState {
    private @Nullable UUID previousVehicleId;

    boolean update(@Nullable UUID currentVehicleId, boolean isHorse) {
        boolean mountedNewHorse = isHorse && !Objects.equals(previousVehicleId, currentVehicleId);
        previousVehicleId = currentVehicleId;
        return mountedNewHorse;
    }
}
