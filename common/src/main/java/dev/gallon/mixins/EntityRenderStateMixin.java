package dev.gallon.mixins;

import dev.gallon.AboveHeadRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(EntityRenderState.class)
public abstract class EntityRenderStateMixin implements AboveHeadRenderState {
    @Unique
    private @NotNull List<Component> horseStatsMod$aboveHeadLines = List.of();

    @Unique
    private @Nullable Vec3 horseStatsMod$aboveHeadAttachment;

    @Override
    public @NotNull List<Component> horseStatsMod$getAboveHeadLines() {
        return horseStatsMod$aboveHeadLines;
    }

    @Override
    public @Nullable Vec3 horseStatsMod$getAboveHeadAttachment() {
        return horseStatsMod$aboveHeadAttachment;
    }

    @Override
    public void horseStatsMod$setAboveHead(@NotNull List<Component> lines, @Nullable Vec3 attachment) {
        horseStatsMod$aboveHeadLines = lines;
        horseStatsMod$aboveHeadAttachment = attachment;
    }
}
