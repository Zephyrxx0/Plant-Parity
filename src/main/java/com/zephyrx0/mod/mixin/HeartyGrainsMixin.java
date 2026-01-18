package com.zephyrx0.mod.mixin;

import com.cobblemon.mod.common.block.HeartyGrainsBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeartyGrainsBlock.class)
public abstract class HeartyGrainsMixin {

    @Inject(
            method = "canPlantOnTop",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void allowRichSoilFarmland(
            BlockState floor,
            BlockView world,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Identifier id = Registries.BLOCK.getId(floor.getBlock());

        if (id != null && id.equals(
                new Identifier("farmersdelight", "rich_soil_farmland")
        )) {
            cir.setReturnValue(true);
        }
    }
}
