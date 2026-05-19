package com.zxy.carpet_wh_addition.mixin.openInv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(value = Player.class)
public class MixinPlayer {
    //#if MC > 12106

    //#else
    //$$ @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;stillValid(Lnet/minecraft/world/entity/player/Player;)Z"),method = "tick")
    //$$ public boolean onTick(AbstractContainerMenu instance, Player player, Operation<Boolean> original){
    //$$    if (player instanceof ServerPlayer) {
    //$$          for (ServerPlayer serverPlayer : OpenInventoryPacket.playerlist) {
    //$$              if (serverPlayer.equals(player)) return true;
    //$$          }
    //$$      }
    //$$    return instance.stillValid(player);
    //$$ }
    //#endif
}
