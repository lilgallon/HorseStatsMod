package dev.gallon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Implemented by every entity render state (see {@code EntityRenderStateMixin}) to carry the statistics displayed
 * above a mount's head from the extraction to the submission of the frame.
 */
public interface AboveHeadRenderState {
    @NotNull List<Component> horseStatsMod$getAboveHeadLines();

    @Nullable Vec3 horseStatsMod$getAboveHeadAttachment();

    void horseStatsMod$setAboveHead(@NotNull List<Component> lines, @Nullable Vec3 attachment);
}
