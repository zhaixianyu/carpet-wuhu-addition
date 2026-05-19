package com.zxy.carpet_wh_addition.mixin.openInv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.zxy.carpet_wh_addition.TickList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

import static com.zxy.carpet_wh_addition.OpenInventoryPacket.playerlist;
import static com.zxy.carpet_wh_addition.OpenInventoryPacket.tickMap;
import static com.zxy.carpet_wh_addition.featuresList.AutoMending.mending;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {
    @Inject(at = @At("HEAD"), method = "disconnect")
    public void onDisconnect(CallbackInfo ci) {
        deletePlayerList();
    }
    @Inject(at = @At("HEAD"),method = "doCloseContainer")
    public void closeHandledScreen(CallbackInfo ci) {
        deletePlayerList();
    }
    @Unique
    private void deletePlayerList(){
        playerlist.removeIf(player -> player.getUUID().equals( ((ServerPlayer)(Object)this).getUUID()));
        tickMap.entrySet().removeIf(k -> k.getKey().getUUID().equals(((ServerPlayer)(Object)this).getUUID()));
//        List<Map.Entry<ServerPlayerEntity, TickList>> list = tickMap.entrySet().stream().filter(k -> k.getKey().getUuid().equals(getUuid())).toList();
//        for (Map.Entry<ServerPlayerEntity, TickList> serverPlayerEntityTickListEntry : list) {
//            tickMap.remove(serverPlayerEntityTickListEntry.getKey());
//        }
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;stillValid(Lnet/minecraft/world/entity/player/Player;)Z"),method = "tick")
    public boolean onTick(AbstractContainerMenu instance, Player playerEntity, Operation<Boolean> original){
        if (playerEntity instanceof ServerPlayer) {
            for (ServerPlayer serverPlayerEntity : playerlist) {
                if (serverPlayerEntity.equals(playerEntity)) return true;
            }
        }
        return instance.stillValid(playerEntity);
    }
    @Inject(at = @At("TAIL"),method = "tick")
    public void tick(CallbackInfo ci){
        if (((ServerPlayer)(Object)this).level().getGameTime() % 20 == 0) {
            mending((ServerPlayer) (Object)this);
        }
    }
}
