package com.zxy.carpet_wh_addition.mixin.openInv;

import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {AbstractFurnaceBlockEntity.class,
        LootableContainerBlockEntity.class,
        BrewingStandBlockEntity.class
})
public class MixinLootableContainerBlockEntity extends BlockEntity {
    public MixinLootableContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //#if MC > 12004
    //$$
    //#else
    @Inject(at = @At("HEAD"), method = "canPlayerUse", cancellable = true)
    public void canPlayerUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        for (ServerPlayerEntity player1 : OpenInventoryPacket.playerlist) {
            if (player.equals(player1)) cir.setReturnValue(true);
        }
    }
    //#endif
}