package dev.gallon.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.gallon.AboveHeadRenderState;
import dev.gallon.HorseStatsMod;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    /**
     * Height of a vanilla name tag line, in name tag pixels.
     */
    @Unique
    private static final int horseStatsMod$LINE_HEIGHT = 10;

    @Inject(method = "extractRenderState", at = @At("TAIL"), remap = false)
    private void horseStatsMod$extractAboveHeadStats(T entity, S state, float partialTicks, CallbackInfo ci) {
        List<Component> lines = HorseStatsMod.instance()
                .map(mod -> mod.getAboveHeadLines(entity, state.distanceToCameraSq))
                .orElse(List.of());
        Vec3 attachment = lines.isEmpty()
                ? null
                : entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getYRot(partialTicks));

        ((AboveHeadRenderState) state).horseStatsMod$setAboveHead(lines, attachment);
    }

    @Inject(method = "submit", at = @At("TAIL"), remap = false)
    private void horseStatsMod$submitAboveHeadStats(
            S state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera,
            CallbackInfo ci
    ) {
        AboveHeadRenderState aboveHead = (AboveHeadRenderState) state;
        List<Component> lines = aboveHead.horseStatsMod$getAboveHeadLines();
        Vec3 attachment = aboveHead.horseStatsMod$getAboveHeadAttachment();
        if (lines.isEmpty() || attachment == null) {
            return;
        }

        // Stack the statistics above the vanilla name tag (custom name) when there is one.
        int vanillaLines = (state.nameTag != null ? 1 : 0) + (state.scoreText != null ? 1 : 0);
        int bottomOffset = -horseStatsMod$LINE_HEIGHT * vanillaLines;
        for (int i = 0; i < lines.size(); i++) {
            int linesBelow = lines.size() - 1 - i;
            submitNodeCollector.submitNameTag(
                    poseStack,
                    attachment,
                    bottomOffset - horseStatsMod$LINE_HEIGHT * linesBelow,
                    lines.get(i),
                    !state.isDiscrete,
                    state.lightCoords,
                    camera
            );
        }
    }
}
