package com.zephyrx0.mod.mixin;

import com.cobblemon.mod.common.block.HeartyGrainsBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeartyGrainsBlock.class)
public abstract class HeartyGrainsMixin {

    @Inject(
            method = "canSurvive",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void allowRichSoilFarmland(
            BlockState state,
            WorldView world,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        BlockState below = world.getBlockState(pos.down());
        Identifier id = Registries.BLOCK.getId(below.getBlock());

        if (id != null && id.equals(
                new Identifier("farmersdelight", "rich_soil_farmland")
        )) {
            cir.setReturnValue(true);
        }
    }
}
