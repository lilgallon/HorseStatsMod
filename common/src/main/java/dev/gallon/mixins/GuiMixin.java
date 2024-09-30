package dev.gallon.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V", at = @At("HEAD"), cancellable = true)
    void preventOverlayMessageUpdate(Component component, boolean bl, CallbackInfo ci) {
        String dismountMessage = Component.translatable(
                "mount.onboard",
                Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage()
        ).getString();

        if (component.getString().equals(dismountMessage)) {
            ci.cancel();
        }
    }
}
