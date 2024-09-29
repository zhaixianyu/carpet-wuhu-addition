package com.zxy.carpet_wh_addition.mixin.openInv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(value = PlayerEntity.class)
public class MixinPlayerEntity {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"),method = "tick")
    public boolean onTick(ScreenHandler instance, PlayerEntity playerEntity, Operation<Boolean> original){
        if (playerEntity instanceof ServerPlayerEntity) {
              for (ServerPlayerEntity serverPlayerEntity : OpenInventoryPacket.playerlist) {
                  if (serverPlayerEntity.equals(playerEntity)) return true;
              }
          }
        return instance.canUse(playerEntity);
    }
}
