package com.zephyrx0.mod.mixin;

import com.cobblemon.mod.common.block.HeartyGrainsBlock;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeartyGrainsBlock.class)
public abstract class HeartyGrainsMixin {

    @Unique
    private static final Identifier RICH_SOIL_FARMLAND = new Identifier("farmersdelight", "rich_soil_farmland");

    @Inject(
            method = "getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void allowRichSoilFarmlandPlacement(
            ItemPlacementContext context,
            CallbackInfoReturnable<BlockState> cir
    ) {
        // If Cobblemon already allows placement, do nothing
        if (cir.getReturnValue() != null) {
            return;
        }

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockPos belowPos = pos.down();

        BlockState belowState = world.getBlockState(belowPos);
        Identifier belowId = Registries.BLOCK.getId(belowState.getBlock());

        // Explicitly allow Farmer's Delight rich soil farmland
        if (RICH_SOIL_FARMLAND.equals(belowId)) {
            // Use default state so placement succeeds
            BlockState self = ((HeartyGrainsBlock) (Object) this).getDefaultState();
            cir.setReturnValue(self);
        }
    }

    @Inject(
            method = "getStateForNeighborUpdate",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventBreakingOnRichSoil(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos,
            CallbackInfoReturnable<BlockState> cir
    ) {
        // Only check when the block below is updated
        if (direction == Direction.DOWN) {
            Identifier neighborId = Registries.BLOCK.getId(neighborState.getBlock());

            // If the block below is rich soil farmland, don't break
            if (RICH_SOIL_FARMLAND.equals(neighborId)) {
                cir.setReturnValue(state);
            }
        }
    }
}
