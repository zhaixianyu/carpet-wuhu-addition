package com.zxy.carpet_wh_addition.mixin.openInv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.zxy.carpet_wh_addition.TickList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

import static com.zxy.carpet_wh_addition.OpenInventoryPacket.playerlist;
import static com.zxy.carpet_wh_addition.OpenInventoryPacket.tickMap;
import static com.zxy.carpet_wh_addition.featuresList.AutoMending.mending;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity{

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    @Inject(at = @At("HEAD"), method = "onDisconnect")
    public void onDisconnect(CallbackInfo ci) {
        deletePlayerList();
    }
    //#if MC > 11802
    @Inject(at = @At("HEAD"),method = "onHandledScreenClosed")
    //#else
    //$$ @Inject(at = @At("HEAD"),method = "closeScreenHandler")
    //#endif
    public void closeHandledScreen(CallbackInfo ci) {
        deletePlayerList();
    }
    @Unique
    private void deletePlayerList(){
        playerlist.removeIf(player -> player.getUuid().equals(getUuid()));
        tickMap.entrySet().removeIf(k -> k.getKey().getUuid().equals(getUuid()));
//        List<Map.Entry<ServerPlayerEntity, TickList>> list = tickMap.entrySet().stream().filter(k -> k.getKey().getUuid().equals(getUuid())).toList();
//        for (Map.Entry<ServerPlayerEntity, TickList> serverPlayerEntityTickListEntry : list) {
//            tickMap.remove(serverPlayerEntityTickListEntry.getKey());
//        }
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"),method = "tick")
    public boolean onTick(ScreenHandler instance, PlayerEntity playerEntity, Operation<Boolean> original){
        if (playerEntity instanceof ServerPlayerEntity) {
            for (ServerPlayerEntity serverPlayerEntity : playerlist) {
                if (serverPlayerEntity.equals(playerEntity)) return true;
            }
        }
        return instance.canUse(playerEntity);
    }
    @Inject(at = @At("TAIL"),method = "tick")
    public void tick(CallbackInfo ci){
        if (this.getWorld().getTime() % 20 == 0) {
            mending((ServerPlayerEntity) (Object)this);
        }
    }
}
