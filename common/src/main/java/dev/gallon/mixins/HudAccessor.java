package dev.gallon.mixins;

import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Hud.class)
public interface HudAccessor {
    @Accessor("overlayMessageTime")
    void setOverlayMessageTime(int overlayMessageTime);
}
