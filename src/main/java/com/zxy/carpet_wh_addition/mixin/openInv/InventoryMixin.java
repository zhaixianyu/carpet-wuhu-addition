package com.zxy.carpet_wh_addition.mixin.openInv;

//#if MC > 11802

import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public interface InventoryMixin {
    @Inject(at = @At("HEAD"), method =
       //#if MC < 12005
       //$$ "canPlayerUse(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/player/PlayerEntity;I)Z"
       //#else
       "canPlayerUse(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/player/PlayerEntity;F)Z"
       //#endif
            , cancellable = true)
    private static void canPlayeruse(
       //#if MC < 12005
       //$$ BlockEntity blockEntity, PlayerEntity player, int range, CallbackInfoReturnable<Boolean> cir
       //#else
       BlockEntity blockEntity, PlayerEntity player, float range, CallbackInfoReturnable<Boolean> cir
       //#endif
    ) {
        if (player instanceof ServerPlayerEntity) {
            for (ServerPlayerEntity serverPlayerEntity : OpenInventoryPacket.playerlist) {
                if (serverPlayerEntity.equals(player)) cir.setReturnValue(true);
            }
        }
    }
}
//#endif