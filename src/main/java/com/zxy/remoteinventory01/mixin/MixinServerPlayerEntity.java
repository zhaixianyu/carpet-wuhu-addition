package com.zxy.remoteinventory01.mixin;

import com.mojang.authlib.GameProfile;
import com.zxy.remoteinventory01.OpenInventoryPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zxy.remoteinventory01.OpenInventoryPacket.playerlist;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity{

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    @Inject(at = @At("HEAD"),method = "onHandledScreenClosed")
    public void closeHandledScreen(CallbackInfo ci) {
        playerlist.removeIf(player -> player.getEntityName().equals(getEntityName()));
    }
}
