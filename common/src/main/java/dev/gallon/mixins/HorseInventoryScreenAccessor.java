package dev.gallon.mixins;

import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HorseInventoryScreen.class)
public interface HorseInventoryScreenAccessor extends AbstractContainerScreenAccessor {
    @Accessor
    AbstractHorse getHorse();
}
